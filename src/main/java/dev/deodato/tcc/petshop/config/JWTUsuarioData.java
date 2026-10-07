package dev.deodato.tcc.petshop.config;

import lombok.Builder;

@Builder
public record JWTUsuarioData(
        Long usuarioId,
        String email
) {
}
