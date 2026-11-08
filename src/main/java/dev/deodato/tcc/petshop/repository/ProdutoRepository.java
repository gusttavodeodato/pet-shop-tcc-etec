package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByFornecedorId(Long id);

    @Query("select p from Produto p where p.saldo <= p.quantMinima")
    List<Produto> findEstoqueBaixo();
}
