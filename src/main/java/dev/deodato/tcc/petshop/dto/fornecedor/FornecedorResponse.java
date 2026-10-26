package dev.deodato.tcc.petshop.dto.fornecedor;

import dev.deodato.tcc.petshop.model.Fornecedor;
import dev.deodato.tcc.petshop.model.enums.CategoriaFornecedor;

import java.time.LocalDateTime;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String cnpj,
        String nomeFantasia,
        CategoriaFornecedor categoria,
        String telefone,
        String email,
        LocalDateTime criadoEm
) {
    public static FornecedorResponse fromEntity(Fornecedor fornecedor) {
        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getNomeFantasia(),
                fornecedor.getCategoria(),
                fornecedor.getTelefone(),
                fornecedor.getEmail(),
                fornecedor.getCriadoEm()
        );
    }
}
