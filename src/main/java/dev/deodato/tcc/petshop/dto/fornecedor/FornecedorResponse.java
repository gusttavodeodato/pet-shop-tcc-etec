package dev.deodato.tcc.petshop.dto.fornecedor;

import dev.deodato.tcc.petshop.model.Fornecedor;

import java.time.LocalDateTime;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String telefone,
        LocalDateTime criadoEm
) {
    public static FornecedorResponse fromEntity(Fornecedor fornecedor) {
        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getNomeFantasia(),
                fornecedor.getCnpj(),
                fornecedor.getTelefone(),
                fornecedor.getCriadoEm()
        );
    }
}
