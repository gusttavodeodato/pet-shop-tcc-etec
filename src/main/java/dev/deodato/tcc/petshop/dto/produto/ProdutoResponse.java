package dev.deodato.tcc.petshop.dto.produto;

import dev.deodato.tcc.petshop.model.Produto;

import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        Integer quantMax,
        Integer quantMininma,
        Integer saldo,
        LocalDateTime criadoEm
) {
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getQuantMax(),
                produto.getQuantMinima(),
                produto.getSaldo(),
                produto.getCriadoEm()
        );
    }
}