package dev.deodato.tcc.petshop.dto.fornecedor;

import dev.deodato.tcc.petshop.model.Fornecedor;
import dev.deodato.tcc.petshop.model.enums.CategoriaFornecedor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record  FornecedorRequest(
        @NotNull(message = "O nome é obrigatório.")
        String razaoSocial,
        @NotNull(message = "O CNPJ é obrigatório")
        @Size(min = 14, max = 14, message = "Deve conter 14 caracteres.")
        String cnpj,

        String nomeFantasia,

        CategoriaFornecedor categoria,


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
                fornecedor.setCnpj(cnpj);
                fornecedor.setNomeFantasia(nomeFantasia);
                fornecedor.setCategoria(categoria);
                fornecedor.setTelefone(telefone);
        }
}
