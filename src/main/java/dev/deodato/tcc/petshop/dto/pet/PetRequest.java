package dev.deodato.tcc.petshop.dto.pet;

import dev.deodato.tcc.petshop.model.Pet;
import jakarta.validation.constraints.NotBlank;

public record PetRequest (
    @NotBlank(message = "O nome do Pet é obrigatório.")
    String nome,

    String raca
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
