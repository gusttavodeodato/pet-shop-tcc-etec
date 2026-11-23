package dev.deodato.tcc.petshop.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UsuarioRegisterRequest(
        @NotEmpty(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @NotEmpty(message = "Senha é obrigatória.")
        @Size(min = 8, max = 72, message = "A senha deve conter entre 8 e 72 caracteres.")
        String senha,

        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome
) {
}
