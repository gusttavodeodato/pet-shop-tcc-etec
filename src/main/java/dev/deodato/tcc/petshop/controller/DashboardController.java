package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.dashboard.RelatorioResponse;
import dev.deodato.tcc.petshop.dto.dashboard.ResumoResponse;
import dev.deodato.tcc.petshop.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
