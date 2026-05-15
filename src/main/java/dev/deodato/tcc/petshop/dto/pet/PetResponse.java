package dev.deodato.tcc.petshop.dto.pet;

import dev.deodato.tcc.petshop.model.Pet;

import java.time.LocalDateTime;

public record PetResponse(
        Long id,
        String nome,
        String raca,
        LocalDateTime criadoEm
) {
    public static PetResponse fromEntity(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getRaca(),
                pet.getCriadoEm()
        );
    }
}
