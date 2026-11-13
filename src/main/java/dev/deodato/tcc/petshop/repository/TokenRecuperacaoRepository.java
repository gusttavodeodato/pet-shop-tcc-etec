package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRecuperacaoRepository extends JpaRepository<TokenRecuperacaoSenha, Long> {

    boolean existsByUsuarioIdAndCriadoEmAfter(Long usuarioId, LocalDateTime criadoEm);

    Optional<TokenRecuperacaoSenha> findByHash(String hash);

    void deleteByUsuarioId(Long usuarioId);

}
