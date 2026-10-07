package dev.deodato.tcc.petshop.dto.usuario;

import jakarta.validation.constraints.NotEmpty;

public record UsuarioRegisterRequest(@NotEmpty(message = "E-mail é obrigatório.") String email,
                                     @NotEmpty(message = "Senha é obrigatória.") String senha) {
}
