package dev.deodato.tcc.petshop.dto.pet;

import dev.deodato.tcc.petshop.dto.tutor.TutorResponse;
import dev.deodato.tcc.petshop.model.Pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PetResponse(
        Long id,
        String nome,
        String raca,
        List<TutorResponse> tutores,
        LocalDateTime criadoEm
) {
    public static PetResponse fromEntity(Pet pet) {
        List<TutorResponse> tutores = pet.getTutores() == null ? List.of() : pet.getTutores().stream()
                .map(TutorResponse::fromEntity).toList();
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getRaca(),
                tutores,
                pet.getCriadoEm()
        );
    }
}
