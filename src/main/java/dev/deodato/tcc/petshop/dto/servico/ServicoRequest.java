package dev.deodato.tcc.petshop.dto.servico;

import dev.deodato.tcc.petshop.model.Servico;

public record ServicoRequest(
        String nome,
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
