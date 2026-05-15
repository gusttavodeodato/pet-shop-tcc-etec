package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.tutor.TutorRequest;
import dev.deodato.tcc.petshop.dto.tutor.TutorResponse;
import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public TutorResponse cadastrarTutor(TutorRequest tutorRequest) {
        if(tutorRequest != null && tutorRepository.existsByEmail(tutorRequest.email())) {
            throw new RuntimeException("Já existe um Tutor cadastrado com esse email.");
        }

        Tutor tutor = tutorRequest.toEntity();
        Tutor tutorSalvo = tutorRepository.save(tutor);
        return TutorResponse.fromEntity(tutorSalvo);
    }

    public Page<TutorResponse> listar(Pageable pageable) {
        return tutorRepository.findAll(pageable).map(TutorResponse::fromEntity);
    }

    public TutorResponse buscarPorId(Long id) {
        Tutor tutor = buscarEntidadePorId(id);
        return TutorResponse.fromEntity(tutor);
    }

    public TutorResponse atualizar(Long id, TutorRequest tutorRequest) {
        Tutor tutor = buscarEntidadePorId(id);
        tutorRequest.preencher(tutor);
        Tutor tutorAtualizado = tutorRepository.save(tutor);
        return TutorResponse.fromEntity(tutorAtualizado);
    }

    public void excluir(Long id) {
        Tutor tutor = buscarEntidadePorId(id);
        tutorRepository.delete(tutor);
    }

    private Tutor buscarEntidadePorId(Long id) {
        return tutorRepository.findById(id).orElseThrow(() -> new RuntimeException("Tutor não encontrado."));
    }
}
