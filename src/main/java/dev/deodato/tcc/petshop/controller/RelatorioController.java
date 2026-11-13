package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.relatorio.AgendamentosResponse;
import dev.deodato.tcc.petshop.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("api/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/agendamentos")
    public AgendamentosResponse agendamentos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM" ) YearMonth mes) {
        return service.agendamentos(mes);
    }
}
