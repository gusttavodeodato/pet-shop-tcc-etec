package dev.deodato.tcc.petshop.dto.movimentacao;

import dev.deodato.tcc.petshop.model.Movimentacao;
import dev.deodato.tcc.petshop.model.enums.TipoOperacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        TipoOperacao tipo,
        Integer quantidade,
        String produto,
        LocalDateTime dataMovimentacao
) {
    public static MovimentacaoResponse fromEntity(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getProduto().getNome(),
                movimentacao.getDataMovimentacao()
        );
    }
}
