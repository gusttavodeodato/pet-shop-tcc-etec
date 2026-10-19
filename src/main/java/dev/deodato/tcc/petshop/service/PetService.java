package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.pet.PetRequest;
import dev.deodato.tcc.petshop.dto.pet.PetResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import dev.deodato.tcc.petshop.repository.MovimentacaoRepository;
import dev.deodato.tcc.petshop.repository.PetRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final TutorService tutorService;

    public PetService(PetRepository petRepository, AgendamentoRepository agendamentoRepository,  TutorService tutorService) {
        this.petRepository = petRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.tutorService = tutorService;
    }

    @Transactional
    public PetResponse cadastrarPet(PetRequest petRequest) {
        Pet pet = petRequest.toEntity();
        petRequest.preencher(pet);
        pet.setTutores(buscarTutores(petRequest.tutorIds()));
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

    @Transactional
    public PetResponse atualizarPetPorId(Long id, PetRequest petRequest) {
        Pet pet = buscarEntidadePorId(id);
        petRequest.preencher(pet);
        pet.setTutores(buscarTutores(petRequest.tutorIds()));
        Pet petAtualizado = petRepository.save(pet);
        return PetResponse.fromEntity(petAtualizado);
    }

    @Transactional
    public void excluirPet(Long id){
        Pet pet = buscarEntidadePorId(id);

        if(agendamentoRepository.existsByPetId(pet.getId())) {
            throw new PetShopException("Não é possível excluir: o pet está agendado.");
        }

        petRepository.delete(pet);
    }

    public List<Tutor> buscarTutores(List<Long> ids) {
        return ids.stream()
                .distinct()
                .map(tutorService::buscarTutorPorId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Pet buscarEntidadePorId(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new PetShopException("Pet não encontrado."));
    }
}
