package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Produto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByFornecedorId(Long id);

    @Query("select p from Produto p where p.saldo <= p.quantMinima")
    List<Produto> findEstoqueBaixo();

    // CONSULTA COM TRAVA, FAZ O BANCO TRAVAR ATÉ O FIM DA TRANSAÇÃO
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id in :ids order by p.id")
    List<Produto> buscarParaVenda(@Param("ids")Collection<Long> ids);

    List<Produto> findByRevendaTrueOrderByNomeAsc();
}
