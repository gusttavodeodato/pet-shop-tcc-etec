package dev.deodato.tcc.petshop.dto.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AtualizarPerfilRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,

        @Pattern(regexp = "^$|^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido. Use o formato (11) 99999-9999.")
        String telefone,

        @Size(max = 255, message = "O endereço dece ter no máximo 255 caracteres.")
        String endereco
) {
}
