package dev.deodato.tcc.petshop.config;

import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Usuario;
import dev.deodato.tcc.petshop.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLogado {

    private final UsuarioRepository usuarios;

    public UsuarioLogado(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario obter() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();

        if(autenticacao == null || !(autenticacao.getPrincipal() instanceof  JWTUsuarioData dados)) {
            throw new PetShopException("Usuário não autenticado.");
        }
        return usuarios.findUsuarioByEmail(dados.email())
                .orElseThrow(() -> new PetShopException("Usuário não encontrado."));
    }
}
