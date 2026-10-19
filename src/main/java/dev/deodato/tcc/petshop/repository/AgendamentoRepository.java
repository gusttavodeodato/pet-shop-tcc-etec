package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByPetAndDataHora(Pet pet, LocalDateTime dataHora);

    Page<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    Page<Agendamento> findByStatusAndDataHoraBetween(StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    boolean existsByServicoId(Long id);
    boolean existsByPetId(Long petId);
}
