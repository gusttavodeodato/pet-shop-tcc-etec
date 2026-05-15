package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.pet.PetRequest;
import dev.deodato.tcc.petshop.dto.pet.PetResponse;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.repository.PetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetResponse cadastrarPet(PetRequest petRequest) {
        if(petRequest.nome() != null) {
            throw new RuntimeException("O nome é obrigatório");
        }

        Pet pet = petRequest.toEntity();
        Pet petSalvo = petRepository.save(pet);
        return PetResponse.fromEntity(petSalvo);
    }

    public Page<PetResponse> listarPets(Pageable pageable) {
        return petRepository.findAll(pageable).map(PetResponse::fromEntity);
    }

    public PetResponse buscarPetPorId(Long id) {
        Pet pet = buscarEntidadePorId(id);
        return PetResponse.fromEntity(pet);
    }

    public PetResponse atualizarPetPorId(Long id, PetRequest petRequest) {
        Pet pet = buscarEntidadePorId(id);
        petRequest.preencher(pet);
        Pet petAtualizado = petRepository.save(pet);
        return PetResponse.fromEntity(petAtualizado);
    }

    public void excluirPet(Long id){
        Pet pet = buscarEntidadePorId(id);
        petRepository.delete(pet);
    }

    private Pet buscarEntidadePorId(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet não encontrado."));
    }
}
