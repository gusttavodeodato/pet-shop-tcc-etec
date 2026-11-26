package dev.deodato.tcc.petshop.dto.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public record AgendamentoRequest(
        @NotNull(message = "A data e o horário são obrigatórios.")
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime dataHora,


        String observacao,

        @NotNull(message = "O ID do pet é obrigatório.")
        Long petID,

        @Size(max = 10, message = "Um atendimento pode ter no máximo 10 serviços.")
        List<Long> servicoIDs
) {

        public List<Long> idsDosServicos() {
                Set<Long> ids = new LinkedHashSet<>();
                if(servicoIDs != null) {
                        servicoIDs.stream().filter(Objects::nonNull).forEach(ids::add);
                }
                return new ArrayList<>(ids);
        }

}
