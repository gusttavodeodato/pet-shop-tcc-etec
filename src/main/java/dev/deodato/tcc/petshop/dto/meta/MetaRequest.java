package dev.deodato.tcc.petshop.dto.meta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MetaRequest(
        @NotNull(message = "O valor da meta é obrigatório,")
        @Positive(message = "O valor da meta deve ser maior que zero.")
        BigDecimal valor) {
}
