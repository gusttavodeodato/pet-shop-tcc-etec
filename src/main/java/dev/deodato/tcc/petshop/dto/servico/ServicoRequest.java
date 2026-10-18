package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.TipoServico;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank(message = "O nome do tipo de serviço deve ser informado.")
        String nome,

        @NotNull(message = "O tipo de serviço é obrigatório.")
        TipoServico tipoServico,

        @NotNull(message = "O preço é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        BigDecimal preco,

        @NotNull(message = "A duração é obrigatória.")
        @Positive(message = "A duração deve ser maior que zero.")
        Integer duracaoMinutos,

        @Size(max = 250, message = "O serviço deve ter no máximo 250 caracteres.")
        String descricao

        ) {
    public Servico toEntity () {
        Servico servico = new Servico();
        preencher(servico);
        return servico;
    }

    public void preencher (Servico servico) {
        servico.setNome(nome);
        servico.setTipoServico(tipoServico);
        servico.setPreco(preco);
        servico.setDuracaoMinutos(duracaoMinutos);
        servico.setDescricao(descricao);
    }
}
