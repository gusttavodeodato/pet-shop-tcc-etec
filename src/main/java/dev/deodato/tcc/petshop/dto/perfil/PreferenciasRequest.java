package dev.deodato.tcc.petshop.dto.perfil;

public record PreferenciasRequest(
        Boolean notificacoesEmail,
        Boolean modoEscuro
) {
}
