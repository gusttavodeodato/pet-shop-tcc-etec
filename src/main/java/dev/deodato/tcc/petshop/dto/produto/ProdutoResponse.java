package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
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
                produto.getQuantMax(),
                produto.getQuantMinima(),
                produto.getSaldo(),
                produto.getPreco(),
                produto.getCriadoEm()
        );
    }
}