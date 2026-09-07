package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}
