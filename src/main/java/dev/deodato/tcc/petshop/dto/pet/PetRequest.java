package dev.deodato.tcc.petshop.dto.pet;

import dev.deodato.tcc.petshop.model.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PetRequest (
    @NotBlank(message = "O nome do Pet é obrigatório.")
    String nome,

    String raca,

    @NotEmpty(message = "O ID do tutor é obrigatório.")
    List<Long> tutorIds
) {
    public Pet toEntity() {
        Pet pet = new Pet();
        preencher(pet);
        return pet;
    }

    public void preencher (Pet pet) {
        pet.setNome(nome);
        pet.setRaca(raca);
    }
}
