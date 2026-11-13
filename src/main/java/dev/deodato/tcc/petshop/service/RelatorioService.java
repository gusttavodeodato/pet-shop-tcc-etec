package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.relatorio.AgendamentosResponse;
import dev.deodato.tcc.petshop.dto.relatorio.ClientesPetsResponse;
import dev.deodato.tcc.petshop.dto.relatorio.ServicoPopular;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import dev.deodato.tcc.petshop.repository.PetRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RelatorioService {

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
