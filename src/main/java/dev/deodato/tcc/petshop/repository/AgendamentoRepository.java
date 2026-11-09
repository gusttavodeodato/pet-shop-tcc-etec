package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByPetAndDataHora(Pet pet, LocalDateTime dataHora);

    Page<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    Page<Agendamento> findByStatusAndDataHoraBetween(StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    List<Agendamento> findByStatusAndDataHoraBetween(StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByDataHoraBetweenOrderByDataHoraAsc(LocalDateTime inicio, LocalDateTime fim);

    long countByStatusAndDataHoraBetween(StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByPetAndDataHoraAndIdNot(Pet pet, LocalDateTime dataHora, Long id);
    boolean existsByServicoId(Long id);
    boolean existsByPetId(Long petId);
}
