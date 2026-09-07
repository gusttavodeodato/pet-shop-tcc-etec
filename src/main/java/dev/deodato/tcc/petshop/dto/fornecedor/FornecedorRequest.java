package dev.deodato.tcc.petshop.dto.fornecedor;

import dev.deodato.tcc.petshop.model.Fornecedor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
        @NotNull(message = "O nome é obrigatório.")
        String razaoSocial,
        String nomeFantasia,

        @NotNull(message = "O CNPJ é obrigatório")
        @Size(min = 14, max = 14, message = "Deve conter 14 caracteres.")
        String cnpj,

        @Size(max = 30, message = "O telefone deve conter no máximo 30 caracteres.")
        String telefone
) {
        public Fornecedor toEntity() {
                Fornecedor fornecedor = new Fornecedor();
                preencher(fornecedor);
                return fornecedor;
        }

        public void preencher (Fornecedor fornecedor) {
                fornecedor.setRazaoSocial(razaoSocial);
                fornecedor.setNomeFantasia(nomeFantasia);
                fornecedor.setCnpj(cnpj);
                fornecedor.setTelefone(telefone);
        }
}
