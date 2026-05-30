package dev.deodato.tcc.petshop.dto.agendamento;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        LocalDateTime dataHora,
        String observacao,
        StatusAgendamento status,
        String pet,
        String servico
) {
}
