package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByPetAndDataHora(Pet pet, LocalDateTime dataHora);
}
