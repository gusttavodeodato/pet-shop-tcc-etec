package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;
import jakarta.validation.constraints.NotBlank;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        Integer quantMax,
        Integer quantMinima,
        Integer saldo
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        preencher(produto);
        return produto;
    }
    public void preencher (Produto produto) {
        produto.setNome(nome);
        produto.setQuantMax(quantMax);
        produto.setQuantMinima(quantMinima);
        produto.setSaldo(saldo);
    }
}
