package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.TipoServico;

import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        String nome,
        TipoServico tipo,
        LocalDateTime criadoEm,
        String descricao
) {
    public static ServicoResponse fromEntity(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getTipoServico(),
                servico.getCriadoEm(),
                servico.getDescricao()
        );
    }
}
