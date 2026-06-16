package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServicoRequest(
        @NotBlank(message = "O nome do tipo de serviço deve ser informado.")
        String nome,

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
        servico.setDescricao(descricao);
    }
}
