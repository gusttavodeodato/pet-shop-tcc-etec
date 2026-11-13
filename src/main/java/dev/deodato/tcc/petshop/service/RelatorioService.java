package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.relatorio.AgendamentosResponse;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

@Service
@Transactional(readOnly = true)
public class RelatorioService {

    private final AgendamentoRepository agendamentos;

    public RelatorioService(AgendamentoRepository agendamentos) {
    this.agendamentos = agendamentos;
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

    private long contar(StatusAgendamento status, LocalDateTime de, LocalDateTime ate) {
        return agendamentos.countByStatusAndDataHoraBetween(status, de, ate);
    }
}
