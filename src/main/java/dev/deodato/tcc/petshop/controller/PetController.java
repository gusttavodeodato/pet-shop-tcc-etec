package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> listarPets() {
        return petService.listarPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPetId(@PathVariable Long id) {
        return petService.buscarPetId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pet criarPet(@RequestBody Pet pet) {
        return petService.salvarPet(pet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPetId(@PathVariable Long id) {
        petService.deletarPet(id);
        return ResponseEntity.noContent().build();
    }
}
