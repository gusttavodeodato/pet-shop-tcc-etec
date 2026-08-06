package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
