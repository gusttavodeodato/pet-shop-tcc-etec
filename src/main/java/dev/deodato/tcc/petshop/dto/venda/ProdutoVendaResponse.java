package dev.deodato.tcc.petshop.dto.venda;

import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;

import java.math.BigDecimal;

public record ProdutoVendaResponse(
        Long id,
        String nome,
        BigDecimal preco,
        CategoriaProduto categoria,
        int saldo,
        Situacao situacao
) {
    public enum Situacao { NORMAL, ESTOQUE_BAIXO, ESGOTADO}
}
