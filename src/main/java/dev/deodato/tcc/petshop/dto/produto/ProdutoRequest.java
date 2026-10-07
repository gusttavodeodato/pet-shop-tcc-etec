package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        Integer quantMax,
        Integer quantMinima,
        Integer saldo,

        @NotNull(message = "O ID do fornecedor é obrigatório.")
        Long fornecedorId
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
