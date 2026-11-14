package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        CategoriaProduto categoria,

        Integer quantMax,
        Integer quantMinima,

        @NotNull(message = "O preço do produto é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        BigDecimal preco,

        @NotNull(message = "O ID do fornecedor é obrigatório.")
        Long fornecedorId,

        Boolean revenda,

        @PositiveOrZero(message = "O custo não pode ser negativo.")
        BigDecimal precoCusto
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        preencher(produto);
        return produto;
    }
    public void preencher (Produto produto) {
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setQuantMax(quantMax);
        produto.setQuantMinima(quantMinima);
        produto.setPreco(preco);

        if(revenda != null) {
            produto.setRevenda(revenda);
        }

        if(precoCusto != null) {
            produto.setPrecoCusto(precoCusto);
        }
    }
}
