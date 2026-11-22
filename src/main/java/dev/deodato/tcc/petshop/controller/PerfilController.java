package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.perfil.AlterarSenhaRequest;
import dev.deodato.tcc.petshop.dto.perfil.AtualizarPerfilRequest;
import dev.deodato.tcc.petshop.dto.perfil.PerfilResponse;
import dev.deodato.tcc.petshop.dto.perfil.PreferenciasRequest;
import dev.deodato.tcc.petshop.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @GetMapping
    public PerfilResponse obter() {
        return service.obter();
    }

    @PutMapping
    public PerfilResponse atualizar(@RequestBody @Valid AtualizarPerfilRequest request) {
        return service.atualizar(request);
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaRequest request) {
        service.alterarSenha(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/preferencias")
    public PerfilResponse.Preferencias preferencias(@RequestBody PreferenciasRequest request) {
        return service.atualizarPreferencias(request);
    }
}
