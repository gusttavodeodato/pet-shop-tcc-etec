package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByTutoresId(Long tutorId);
    long countByCriadoEmGreaterThanEqual(LocalDateTime desde);
}
