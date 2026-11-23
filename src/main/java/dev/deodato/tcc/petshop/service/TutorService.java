package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.tutor.TutorRequest;
import dev.deodato.tcc.petshop.dto.tutor.TutorResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.repository.PetRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;

    public TutorService(TutorRepository tutorRepository,  PetRepository petRepository) {
        this.tutorRepository = tutorRepository;
        this.petRepository = petRepository;
    }

    public TutorResponse cadastrarTutor(TutorRequest tutorRequest) {
        if(temEmail(tutorRequest.email()) && tutorRepository.existsByEmail(tutorRequest.email().trim())) {
            throw new PetShopException("Já existe um Tutor cadastrado com esse e-mail.");
        }

        Tutor tutor = tutorRequest.toEntity();
        Tutor tutorSalvo = tutorRepository.save(tutor);
        return TutorResponse.fromEntity(tutorSalvo);
    }

    public Page<TutorResponse> listar(Pageable pageable) {
        return tutorRepository.findAll(pageable).map(TutorResponse::fromEntity);
    }

    public TutorResponse buscarPorId(Long id) {
        Tutor tutor = buscarTutorPorId(id);
        return TutorResponse.fromEntity(tutor);
    }

    public TutorResponse atualizar(Long id, TutorRequest tutorRequest) {
        Tutor tutor = buscarTutorPorId(id);

        if(temEmail(tutorRequest.email()) && tutorRepository.existsByEmailAndIdNot(tutorRequest.email().trim(), id)) {
            throw new PetShopException("Já existe Tutor cadastrado com esse e-mail.");
        }

        tutorRequest.preencher(tutor);
        Tutor tutorAtualizado = tutorRepository.save(tutor);
        return TutorResponse.fromEntity(tutorAtualizado);
    }

    public void excluir(Long id) {
        Tutor tutor = buscarTutorPorId(id);
        if(petRepository.existsByTutoresId(tutor.getId())) {
            throw new PetShopException("Não é possível excluir: tutor vinculado com pet.");
        }
        tutorRepository.delete(tutor);
    }

    private boolean temEmail(String email) {
        return email != null && !email.isBlank();
    }

    public Tutor buscarTutorPorId(Long id) {
        return tutorRepository.findById(id).orElseThrow(() -> new PetShopException("Tutor não encontrado."));
    }
}
