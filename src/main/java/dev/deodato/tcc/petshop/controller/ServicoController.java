package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.servico.ServicoRequest;
import dev.deodato.tcc.petshop.dto.servico.ServicoResponse;
import dev.deodato.tcc.petshop.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoResponse criarServico(@RequestBody @Valid ServicoRequest servicoRequest) {
        return servicoService.criarServico(servicoRequest);
    }

    @GetMapping
    public Page<ServicoResponse> listarServicos(Pageable pageable) {
        return servicoService.listarServicos(pageable);
    }

    @GetMapping("/{id}")
    public ServicoResponse buscarServicoPorId(@PathVariable Long id) {
        return servicoService.buscarServicoPorId(id);
    }

    @PutMapping("/{id}")
    public ServicoResponse atualizarServicoPorId(@PathVariable Long id, @RequestBody @Valid ServicoRequest servicoRequest) {
        return servicoService.atualizarServicoPorId(id, servicoRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirServicoPorId(@PathVariable Long id) {
        servicoService.excluirServico(id);
    }
}
