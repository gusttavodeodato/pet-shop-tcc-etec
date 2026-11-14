package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.relatorio.*;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import dev.deodato.tcc.petshop.repository.PetRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
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
