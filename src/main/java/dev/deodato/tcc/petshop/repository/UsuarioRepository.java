package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<UserDetails> findByEmail(String email);
    boolean existsByEmail(String email);
}
