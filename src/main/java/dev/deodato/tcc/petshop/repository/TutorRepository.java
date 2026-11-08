package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByEmail(String email);

    long countByCriadoEmGreaterThanEqual(LocalDateTime desde);
}
