package dev.deodato.tcc.petshop.config;

import dev.deodato.tcc.petshop.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthConfig implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
