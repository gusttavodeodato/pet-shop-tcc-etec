package dev.deodato.tcc.petshop.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dev.deodato.tcc.petshop.dto.relatorio.*;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import dev.deodato.tcc.petshop.repository.PetRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RelatorioService {

    private static final ZoneId BRASIL = ZoneId.of("America/Sao_Paulo");
    private final AgendamentoRepository agendamentos;
    private final TutorRepository tutores;
    private final PetRepository pets;

    public RelatorioService(AgendamentoRepository agendamentos, TutorRepository tutores, PetRepository pets) {
        this.agendamentos = agendamentos;
        this.tutores = tutores;
        this.pets = pets;
    }

    public AgendamentosResponse agendamentos(YearMonth mes) {
        LocalDateTime de = mes.atDay(1).atStartOfDay();
        LocalDateTime ate = mes.atEndOfMonth().atTime(LocalTime.MAX);

        long concluidos = contar(StatusAgendamento.CONCLUIDO, de, ate);
        long cancelados = contar(StatusAgendamento.CANCELADO, de, ate);
        long agendados = contar(StatusAgendamento.AGENDADO, de, ate)
                + contar(StatusAgendamento.CONFIRMADO, de, ate)
                + contar(StatusAgendamento.EM_ANDAMENTO, de, ate);

        return new AgendamentosResponse(concluidos + agendados + cancelados, concluidos, agendados, cancelados);
    }

    public ClientesPetsResponse clientePets(YearMonth mes) {
        LocalDateTime de = inicio(mes);
        LocalDateTime ate = fim(mes);

        return new ClientesPetsResponse(
                new ClientesPetsResponse.Contagem(tutores.count(),
                        tutores.countByCriadoEmBetween(de, ate)),
                new ClientesPetsResponse.Contagem(pets.count(),
                        pets.countByCriadoEmBetween(de, ate))
        );

    }

    public List<ServicoPopular> servicosPopulares(YearMonth mes, int limite) {
        List<Agendamento> concluidos = agendamentos
                .findByStatusAndDataHoraBetween(StatusAgendamento.CONCLUIDO, inicio(mes), fim(mes));

        return concluidos.stream()
                .filter(a -> a.getServico() != null && a.getServico().getNome() != null)
                .collect(Collectors.groupingBy(a -> a.getServico().getNome()))
                .entrySet().stream()
                .map(e -> new ServicoPopular(e.getKey(), e.getValue().size(), somar(e.getValue())))
                .sorted(Comparator.comparingLong(ServicoPopular::quantidade).reversed()
                    .thenComparing(ServicoPopular::receita, Comparator.reverseOrder()))
                .limit(limite)
                .toList();
    }

    public ReceitaAcumuladaResponse receitaAcumulada(YearMonth mes) {
        LocalDate hoje = LocalDate.now(BRASIL);
        YearMonth atual = YearMonth.from(hoje);
        YearMonth anterior = mes.minusMonths(1);

        int ultimoDia;
        if(mes.isAfter(atual)) {
            ultimoDia = 0;
        } else if(mes.equals(atual)) {
            ultimoDia = hoje.getDayOfMonth();
        } else {
            ultimoDia = mes.lengthOfMonth();
        }

        return new ReceitaAcumuladaResponse(
                serie(mes, ultimoDia),
                serie(anterior, anterior.lengthOfMonth())
        );
    }

    private List<ReceitaAcumuladaResponse.Ponto> serie(YearMonth mes, int ultimoDia) {
        List<Agendamento> concluidos = agendamentos.findByStatusAndDataHoraBetween(
                StatusAgendamento.CONCLUIDO, inicio(mes), fim(mes)
        );

        Map<Integer, BigDecimal> receitaPorDia = new HashMap<>();
        for(Agendamento a : concluidos) {
            BigDecimal valor = a.getTotal() != null ? a.getTotal() : BigDecimal.ZERO;
            receitaPorDia.merge(a.getDataHora().getDayOfMonth(), valor, BigDecimal::add);
        }

        List<ReceitaAcumuladaResponse.Ponto> pontos = new ArrayList<>();
        BigDecimal acumulado = BigDecimal.ZERO;

        for(int dia = 1; dia <= ultimoDia; dia++) {
            acumulado = acumulado.add(receitaPorDia.getOrDefault(dia, BigDecimal.ZERO));
            pontos.add(new ReceitaAcumuladaResponse.Ponto(dia, acumulado));
        }

        return pontos;
    }

    public HistoricoResponse historico(YearMonth mes, StatusAgendamento status, String busca, int pagina, int tamanho) {
        List<Agendamento> doMes = finalizadosDoMes(mes);

        long concluidos = doMes.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .count();
        long cancelados = doMes.size() - concluidos;

        List<Agendamento> filtrados = filtrar(doMes, status, busca);

        int pag = Math.max(0, Math.min(pagina, 10_000));
        int tam = Math.max(1, Math.min(tamanho, 100));
        int de = (int) Math.min((long) pag * tam, filtrados.size());
        int ate = Math.min(de + tam, filtrados.size());

        List<HistoricoResponse.Linha> linhas = filtrados.subList(de, ate).stream()
                .map(this::paraLinha)
                .toList();

        return new HistoricoResponse(
                new HistoricoResponse.Resumo(doMes.size(), concluidos, cancelados), filtrados.size(), pag, tam, linhas);
    }

    public byte[] exportarPdf(YearMonth mes, StatusAgendamento status, String busca) {
        List<Agendamento> doMes = finalizadosDoMes(mes);
        List<Agendamento> lista = filtrar(doMes, status, busca);

        long concluidos = doMes.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .count();
        long cancelados = doMes.size() - concluidos;

        String nomeMes = mes.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-BR")) + " de " + mes.getYear();

        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 28, 28, 28, 28);

        try {
            PdfWriter.getInstance(documento, saida);
            documento.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font negrito = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font cabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.NORMAL, Color.WHITE);

            documento.add(new Paragraph("Histórico mensal de serviços", titulo));
            documento.add(new Paragraph("Atendimentos finalizados em " + nomeMes, normal));
            documento.add(new Paragraph("Serviços do mês: " + doMes.size()
                        + " | Concluídos: " + concluidos
                        + " | Cancelados: " + cancelados, negrito));
            documento.add(new Paragraph(" ", normal));

            if(lista.isEmpty()) {
                documento.add(new Paragraph("Nenhum atendimento encontrado.", normal));
            } else {
                PdfPTable tabela = new PdfPTable(new float[]{2,4f, 2f, 1.8f, 1.8f, 3.6f, 1.4f, 1.3f});
                tabela.setWidthPercentage(100);
                tabela.setHeaderRows(1);

                for(String texto : List.of("Pet e tutor", "Serviço", "Data e hora", "Observação", "Status", "Valor")) {
                    PdfPCell celula = new PdfPCell(new Phrase(texto, cabecalho));
                    celula.setBackgroundColor(new Color(37, 99, 235));
                    celula.setPadding(5);
                    celula.addElement(celula);
                }

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                for (Agendamento a : lista) {
                    tabela.addCell(celulaPdf(a.getPet().getNome() + "\n" + nomeTutor(a), normal, Element.ALIGN_LEFT));
                    tabela.addCell(celulaPdf(a.getServico().getNome(), normal, Element.ALIGN_LEFT));
                    tabela.addCell(celulaPdf(a.getDataHora().format(formato), normal, Element.ALIGN_LEFT));
                    tabela.addCell(celulaPdf(a.getObservacao(), normal, Element.ALIGN_LEFT));
                    tabela.addCell(celulaPdf(a.getStatus() == StatusAgendamento.CONCLUIDO
                            ? "Concluído" : "Cancelado", normal, Element.ALIGN_LEFT));
                    tabela.addCell(celulaPdf("R$ " + valor(a.getTotal()), normal, Element.ALIGN_RIGHT));
                }
                documento.add(tabela);

                BigDecimal receita = somar(lista.stream()
                        .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                        .toList());
                Paragraph total = new Paragraph("Total dos concluídos listados: R$ " + valor(receita), negrito);
                total.setSpacingBefore(8);
                documento.add(total);

            }

            documento.close();
        } catch (Exception e) {
            throw new PetShopException("Não foi possível gerar o PDF.");
        }
        return saida.toByteArray();
    }

    private List<Agendamento> finalizadosDoMes(YearMonth mes) {
        List<Agendamento> lista = agendamentos
                .findByStatusInAndDataHoraBetweenOrderByDataHoraAsc(List.of(StatusAgendamento.CONCLUIDO, StatusAgendamento.CANCELADO), inicio(mes), fim(mes));

        Map<Long, Agendamento> semRepeticao = new LinkedHashMap<>();
        for(Agendamento a : lista) {
            semRepeticao.putIfAbsent(a.getId(), a );
        }
        return new ArrayList<>(semRepeticao.values());
    }

    private PdfPCell celulaPdf(String texto, Font fonte, int alinhamento) {
        PdfPCell celula = new PdfPCell(new Phrase(texto == null || texto.isBlank() ? "-" : texto, fonte));
        celula.setPadding(4);
        celula.setHorizontalAlignment(alinhamento);
        return celula;
    }

    private List<Agendamento> filtrar(List<Agendamento> lista, StatusAgendamento status, String busca) {
        if(status !=null && status != StatusAgendamento.CONCLUIDO && status != StatusAgendamento.CANCELADO) {
            throw new PetShopException("Filtre por CONCLUIDO ou CANCELADO.");
        }

        String termo = normalizar(busca);

        return lista.stream()
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> termo.isEmpty() || corresponde(a, termo))
                .toList();
    }

    private boolean corresponde(Agendamento a, String termo) {
        List<String> campos = new ArrayList<>();
        campos.add(a.getPet().getNome());
        campos.add(a.getServico().getNome());
        if(a.getPet().getTutores() != null) {
            a.getPet().getTutores().forEach(t -> campos.add(t.getNome()));
        }
        return campos.stream().anyMatch(c -> normalizar(c).contains(termo));
    }

    private String normalizar(String texto) {
        if(texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    private HistoricoResponse.Linha paraLinha(Agendamento a) {
        return new HistoricoResponse.Linha(
                a.getId(),
                a.getPet().getNome(),
                nomeTutor(a),
                a.getServico().getNome(),
                a.getDataHora(),
                a.getObservacao(),
                a.getStatus(),
                a.getTotal());
    }

    private String nomeTutor(Agendamento a) {
        List<Tutor> donos = a.getPet().getTutores();
        return(donos == null || donos.isEmpty()) ? "-" : donos.get(0).getNome();
    }

    private String valor(BigDecimal total) {
        return total == null ? "" : total.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }

    private LocalDateTime inicio(YearMonth mes) {
        return mes.atDay(1).atStartOfDay();
    }

    private LocalDateTime fim(YearMonth mes) {
        return mes.atEndOfMonth().atTime(LocalTime.MAX);
    }

    private long contar(StatusAgendamento status, LocalDateTime de, LocalDateTime ate) {
        return agendamentos.countByStatusAndDataHoraBetween(status, de, ate);
    }

    private BigDecimal somar(List<Agendamento> lista) {
        return lista.stream()
                .map(Agendamento::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
