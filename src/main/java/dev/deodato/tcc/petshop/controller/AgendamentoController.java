package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoRequest;
import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoResponse;
import dev.deodato.tcc.petshop.service.AgendamentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/agendamentos")
public class AgendamentoController {

    AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public AgendamentoResponse criar(@RequestBody AgendamentoRequest request) {
        return agendamentoService.criar(request);
    }

    @PutMapping("{id}")
    public AgendamentoResponse atualizar(@PathVariable Long id, @RequestBody AgendamentoRequest request) {
        return agendamentoService.atualizar(id, request);
    }

    @PutMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar(@PathVariable Long id) {
        return agendamentoService.cancelar(id);
    }

    @PutMapping("/{id}/concluido")
    public AgendamentoResponse concluido(@PathVariable Long id) {
        return agendamentoService.concluido(id);
    }

    @GetMapping("/{id}")
    public AgendamentoResponse buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id);
    }
}
