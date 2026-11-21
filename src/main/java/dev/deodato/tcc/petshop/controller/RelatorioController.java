package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.relatorio.*;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.service.RelatorioService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/relatorios")
@Validated
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

    @GetMapping("/clientes-pets")
    public ClientesPetsResponse clientesPets(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        return service.clientePets(mes);
    }

    @GetMapping("/servicos-populares")
    public List<ServicoPopular> servicoPopulars(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limite) {
        return service.servicosPopulares(mes, limite);
    }

    @GetMapping("/receita-acumulada")
    public ReceitaAcumuladaResponse receitaAcumulada(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        return service.receitaAcumulada(mes);
    }

    @GetMapping("/historico")
    public HistoricoResponse historico(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
            @RequestParam(required = false)StatusAgendamento status,
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        return service.historico(mes, status, busca, pagina, tamanho);
    }

    @GetMapping("/historico/pdf")
    public ResponseEntity<byte[]> historicoPdf(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
            @RequestParam(required = false) StatusAgendamento status,
            @RequestParam(required = false) String busca) {
        byte[] conteudo = service.exportarPdf(mes, status, busca);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"historico-" + mes + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(conteudo);
    }
}
