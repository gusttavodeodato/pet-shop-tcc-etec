package dev.deodato.tcc.petshop.dto.agendamento;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        LocalDateTime dataHora,
        String observacao,
        Long petID,
        Long servicoID
) {

}
