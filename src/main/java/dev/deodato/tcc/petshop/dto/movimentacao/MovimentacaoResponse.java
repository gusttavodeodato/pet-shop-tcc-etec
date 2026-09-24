package dev.deodato.tcc.petshop.dto.movimentacao;

import dev.deodato.tcc.petshop.model.Movimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Enum tipo,
        Integer quantidade,
        String produto,
        LocalDateTime dataMovimentacao
) {
    public static MovimentacaoResponse froEntity(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getProduto(),
                movimentacao.getDataMovimentacao()
        );
    }
}
