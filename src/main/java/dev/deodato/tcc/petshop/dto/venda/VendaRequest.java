package dev.deodato.tcc.petshop.dto.venda;

import dev.deodato.tcc.petshop.model.enums.FormaDePagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record VendaRequest(
        Long tutorId,

        @NotNull(message = "A forma de pagamento é obrigatória.")
        FormaDePagamento formaPagamento,

        @NotEmpty(message = "A venda precisa ter ao menos um item.")
        @Valid
        List<ItemRequest> itens
) {

    public record ItemRequest(
            @NotNull(message = "O produto é obrigatório.")
            Long produtoId,

            @NotNull(message = "A quantidade é obrigatória.")
            @Positive(message = "A quantidade deve ser maior que zero.")
            Integer quantidade
    ) {}
}
