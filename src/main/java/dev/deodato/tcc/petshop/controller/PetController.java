package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.pet.PetRequest;
import dev.deodato.tcc.petshop.dto.pet.PetResponse;
import dev.deodato.tcc.petshop.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse cadastarPet(@RequestBody PetRequest petRequest) {
        return petService.cadastrarPet(petRequest);
    }

    @GetMapping
    public Page<PetResponse> listarPets(Pageable pageable) {
        return petService.listarPets(pageable);
    }

    @GetMapping("/{id}")
    public PetResponse buscarPetPorId(@PathVariable Long id) {
        return petService.buscarPetPorId(id);
    }

    @PutMapping("/{id}")
    public PetResponse atualizarPetPorId(@PathVariable Long id, @RequestBody PetRequest petRequest) {
        return petService.atualizarPetPorId(id, petRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPet(@PathVariable Long id) {
        petService.excluirPet(id);
    }
}
