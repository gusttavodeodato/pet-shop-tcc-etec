package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.auth.EsqueciSenhaRequest;
import dev.deodato.tcc.petshop.dto.auth.RedefinirSenhaRequest;
import dev.deodato.tcc.petshop.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService service;

    public RecuperacaoSenhaController(RecuperacaoSenhaService service) {
        this.service = service;
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> esqueciSenha (@RequestBody @Valid EsqueciSenhaRequest request) {
        service.solicitar(request.email());
        return ResponseEntity.ok(Map.of("mensagem", "Se o e-mail estiver cadastrado, você receberá as instruções em instantes."));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid RedefinirSenhaRequest request) {
        service.redefinir(request.token(), request.novaSenha());
        return ResponseEntity.noContent().build();
    }
}
