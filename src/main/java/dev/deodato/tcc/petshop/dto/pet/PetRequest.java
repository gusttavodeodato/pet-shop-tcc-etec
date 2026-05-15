package dev.deodato.tcc.petshop.dto.pet;

import dev.deodato.tcc.petshop.model.Pet;

public record PetRequest (
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
