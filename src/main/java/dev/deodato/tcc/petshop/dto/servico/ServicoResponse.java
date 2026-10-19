package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.Categoria;
import dev.deodato.tcc.petshop.model.enums.TipoServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        String nome,
        TipoServico tipoServico,
        Categoria categoria,
        BigDecimal preco,
        Integer duracaoMinutos,
        LocalDateTime criadoEm,
        String descricao
) {
    public static ServicoResponse fromEntity(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getTipoServico(),
                servico.getCategoria(),
                servico.getPreco(),
                servico.getDuracaoMinutos(),
                servico.getCriadoEm(),
                servico.getDescricao()
        );
    }
}
