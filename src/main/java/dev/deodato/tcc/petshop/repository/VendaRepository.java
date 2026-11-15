package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
