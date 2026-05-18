package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;

import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        String nome,
        LocalDateTime criadoEm,
        String descricao
) {
    public static ServicoResponse fromEntity(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getCriadoEm(),
                servico.getDescricao()
        );
    }
}
