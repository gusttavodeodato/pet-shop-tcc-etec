package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Venda;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @EntityGraph(attributePaths = {"itens", "itens.produto", "tutor"})
    List<Venda> findByDataHoraBetweenOrderByDataHoraDesc(LocalDateTime inicio, LocalDateTime fim);
}
