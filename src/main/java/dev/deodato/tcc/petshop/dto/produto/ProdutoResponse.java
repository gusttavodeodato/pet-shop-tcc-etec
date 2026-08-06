package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String unidade,
        BigDecimal precoUnitario,
        LocalDateTime criadoEm
) {
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getUnidade(),
                produto.getPrecoUnitario(),
                produto.getCriadoEm()
        );
    }
}