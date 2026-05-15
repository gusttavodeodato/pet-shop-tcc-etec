package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
