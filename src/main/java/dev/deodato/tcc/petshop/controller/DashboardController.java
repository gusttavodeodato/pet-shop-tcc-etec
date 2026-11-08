package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.dashboard.RelatorioResponse;
import dev.deodato.tcc.petshop.dto.dashboard.ResumoResponse;
import dev.deodato.tcc.petshop.dto.meta.MetaRequest;
import dev.deodato.tcc.petshop.service.DashboardService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/relatorio")
    public RelatorioResponse relatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.relatorio(inicio, fim);
    }

    @GetMapping("/resumo")
    public ResumoResponse resumo() {
        return service.resumo();
    }

    @PutMapping("/meta")
    public ResponseEntity<Void> definirMeta(@Valid @RequestBody MetaRequest request) {
        service.definirMeta(request.valor());
        return ResponseEntity.noContent().build();
    }
}
