package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String unidade,
        BigDecimal precoUnitario
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        preencher(produto);
        return produto;
    }
    public void preencher (Produto produto) {
        produto.setNome(nome);
        produto.setUnidade(unidade);
        produto.setPrecoUnitario(precoUnitario);
    }
}
