package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.MetaMensal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaMensalRepository extends JpaRepository<MetaMensal, Long> {
    Optional<MetaMensal> findByAnoAndMes(int ano, int mes);
}
