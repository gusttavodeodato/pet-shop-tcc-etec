package dev.deodato.tcc.petshop.dto.movimentacao;

import dev.deodato.tcc.petshop.model.Movimentacao;
import dev.deodato.tcc.petshop.model.enums.TipoOperacao;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(
        @NotNull(message = "Informe o tipo de movimento")
        TipoOperacao tipo,

        Integer quantidade,

        @NotNull(message = "ID do produto é obrigatório.")
        Long produtoId

) {

    public Movimentacao toEntity() {
        Movimentacao movimentacao = new Movimentacao();
        preencher(movimentacao);
        return movimentacao;
    }

    public void preencher(Movimentacao movimentacao) {
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
    }
}
