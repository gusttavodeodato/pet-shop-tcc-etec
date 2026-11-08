package dev.deodato.tcc.petshop.dto.dashboard;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalTime;

public record AgendaItem (
        Long id,
        LocalTime hora,
        String pet,
        String tutor,
        String servico,
        BigDecimal valor,
        StatusAgendamento status
) {}


