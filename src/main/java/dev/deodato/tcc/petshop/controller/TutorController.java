package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.tutor.TutorRequest;
import dev.deodato.tcc.petshop.dto.tutor.TutorResponse;
import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutores")
public class TutorController {

    private TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TutorResponse cadastrarTutor(@RequestBody TutorRequest tutorRequest) {
        return tutorService.cadastrarTutor(tutorRequest);
    }

    @GetMapping
    public Page<TutorResponse> listar(Pageable pageable) {
        return tutorService.listar(pageable);
    }

    @GetMapping("/{id}")
    public TutorResponse buscarPorId(@PathVariable Long id) {
        return tutorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TutorResponse atualizar(@PathVariable Long id, @RequestBody TutorRequest tutorRequest) {
        return tutorService.atualizar(id, tutorRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        tutorService.excluir(id);
    }

}
