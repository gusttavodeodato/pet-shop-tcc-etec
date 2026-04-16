package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.model.Tutor;
import dev.deodato.tcc.petshop.service.TutorService;
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

    @GetMapping
    public List<Tutor> listarTutores() {
        return tutorService.listarTutores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutor> buscarPorId(@PathVariable Long id) {
        return tutorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tutor criarTutor(@RequestBody Tutor tutor) {
        return tutorService.salvar(tutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        tutorService.remover(id);
        return ResponseEntity.noContent().build();
    }

}
