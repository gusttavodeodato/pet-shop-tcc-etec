package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.dashboard.RelatorioResponse;
import dev.deodato.tcc.petshop.dto.dashboard.ResumoResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.MetaMensal;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {
    private static final ZoneId BRASIL = ZoneId.of("America/Sao_Paulo");

    private final AgendamentoRepository agendamentos;
    private final TutorRepository tutores;
    private final PetRepository pets;
    private final ProdutoRepository produtos;
    private final MetaMensalRepository metas;

    public DashboardService(AgendamentoRepository agendamentos, TutorRepository tutores, PetRepository pets, ProdutoRepository produtos, MetaMensalRepository metas) {
        this.agendamentos = agendamentos;
        this.tutores = tutores;
        this.pets = pets;
        this.produtos = produtos;
        this.metas = metas;
    }

    public RelatorioResponse relatorio(LocalDate inicio, LocalDate fim) {
        if(inicio.isAfter(fim)) {
            throw new PetShopException("A data inicial não pode ser depois da final.");
        }
        LocalDateTime de = inicio.atStartOfDay();
        LocalDateTime ate = fim.atTime(LocalTime.MAX);

        List<Agendamento> concluidos = agendamentos
                .findByStatusAndDataHoraBetween(StatusAgendamento.CONCLUIDO, de, ate);

        long total = concluidos.size();
        BigDecimal receita = somar(concluidos);
        BigDecimal ticketMedio = total == 0
                ? BigDecimal.ZERO
                : receita.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

        List<RelatorioResponse.ServicoPorTipo> porTipo = concluidos.stream()
                .filter(a -> a.getServico() != null && a.getServico().getTipoServico() != null)
                .collect(Collectors.groupingBy(a -> a.getServico().getTipoServico()))
                        .entrySet().stream()
                        .map(e -> new RelatorioResponse.ServicoPorTipo(
                                e.getKey().name(),
                                e.getValue().size(),
                                somar(e.getValue())))
                        .sorted(Comparator.comparingLong(RelatorioResponse.ServicoPorTipo::quantidade).reversed())
                        .toList();

        List<RelatorioResponse.ReceitaPorDia> porDia = concluidos.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDataHora().toLocalDate(),
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Agendamento::getTotal, BigDecimal::add)))
                .entrySet().stream()
                .map(e -> new RelatorioResponse.ReceitaPorDia(e.getKey(), e.getValue()))
                .toList();

        return new RelatorioResponse(total, receita, ticketMedio, porTipo, porDia);
    }

    public ResumoResponse resumo() {
        LocalDate hoje = LocalDate.now(BRASIL);
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = hoje.atTime(LocalTime.MAX);
        LocalDateTime inicioMes = hoje.withDayOfMonth(1).atStartOfDay();
        LocalDateTime fimMes = hoje.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        long agendados =  contar(StatusAgendamento.AGENDADO, inicioDia, fimDia);
        long confirmados = contar(StatusAgendamento.CONFIRMADO, inicioDia, fimDia);
        long emAndamento = contar(StatusAgendamento.EM_ANDAMENTO, inicioDia, fimDia);
        long concluidos = contar(StatusAgendamento.CONCLUIDO, inicioDia, fimDia);
        long cancelados = contar(StatusAgendamento.CANCELADO, inicioDia, fimDia);
        long totalHoje = agendados + confirmados + emAndamento + concluidos + cancelados;

        BigDecimal faturamentoMes = somar(agendamentos.findByStatusAndDataHoraBetween(StatusAgendamento.CONCLUIDO, inicioMes, fimMes));

        BigDecimal valorMeta = metas.findByAnoAndMes(hoje.getYear(), hoje.getMonthValue())
                .map(MetaMensal::getValor)
                .orElse(BigDecimal.ZERO);

        BigDecimal projecao = faturamentoMes
                .multiply(BigDecimal.valueOf(hoje.lengthOfMonth()))
                .divide(BigDecimal.valueOf(hoje.getDayOfMonth()), 2, RoundingMode.HALF_UP);

        List<ResumoResponse.EstoqueBaixo> estoqueBaixo = produtos.findEstoqueBaixo().stream()
                .map(p -> new ResumoResponse.EstoqueBaixo(p.getNome(), p.getSaldo()))
                .toList();

        return new ResumoResponse(
                new ResumoResponse.Contagem(tutores.count(),
                        tutores.countByCriadoEmGreaterThanEqual(inicioMes)),
                new ResumoResponse.Contagem(pets.count(),
                        pets.countByCriadoEmGreaterThanEqual(inicioMes)),
                new ResumoResponse.Hoje(totalHoje, agendados, confirmados, emAndamento, concluidos, cancelados),
                faturamentoMes,
                new ResumoResponse.Meta(valorMeta, projecao),
                estoqueBaixo);
    }

    @Transactional
    public void definirMeta(BigDecimal valor) {
        LocalDate hoje = LocalDate.now(BRASIL);
        MetaMensal meta = metas.findByAnoAndMes(hoje.getYear(), hoje.getMonthValue())
                .orElseGet(() -> new MetaMensal(hoje.getYear(), hoje.getMonthValue(), valor));
        meta.setValor(valor);
        metas.save(meta);
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
