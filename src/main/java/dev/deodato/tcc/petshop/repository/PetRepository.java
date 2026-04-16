package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
