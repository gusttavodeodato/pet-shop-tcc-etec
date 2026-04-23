package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.service.ServicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<Servico> listarServicos() {
        return servicoService.listarServicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarServicoId(@PathVariable Long id) {
        return servicoService.buscarServicoId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Servico criarServico(@RequestBody Servico servico) {
        return servicoService.salvarServico(servico);
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoNovo) {
        return servicoService.atualizarServico(servicoNovo)
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}
