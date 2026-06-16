package dev.deodato.tcc.petshop.dto.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull(message = "A data e o horário são obrigatórios.")
        @Future(message = "O agendamento deve ser no futuro")
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime dataHora,

        String observacao,

        @NotNull(message = "O ID do pet é obrigatório.")
        Long petID,

        @NotNull(message =  "O ID do serviço é obrigatório.")
        Long servicoID
) {

}
