package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
