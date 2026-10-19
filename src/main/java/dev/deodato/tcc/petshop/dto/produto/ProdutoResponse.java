package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        CategoriaProduto categoria,
        Integer quantMax,
        Integer quantMinima,
        Integer saldo,
        BigDecimal preco,
        LocalDateTime criadoEm
) {
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getQuantMax(),
                produto.getQuantMinima(),
                produto.getSaldo(),
                produto.getPreco(),
                produto.getCriadoEm()
        );
    }
}