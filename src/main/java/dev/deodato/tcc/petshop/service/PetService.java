package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> buscarPetId(@PathVariable Long id) {
        return petRepository.findById(id);
    }

    public Pet salvarPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletarPet(@PathVariable Long id) {
        petRepository.deleteById(id);
    }
}
