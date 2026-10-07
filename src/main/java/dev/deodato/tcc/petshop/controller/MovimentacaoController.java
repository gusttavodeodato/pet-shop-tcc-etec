package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.movimentacao.MovimentacaoRequest;
import dev.deodato.tcc.petshop.dto.movimentacao.MovimentacaoResponse;
import dev.deodato.tcc.petshop.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {this.movimentacaoService = movimentacaoService;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@Valid @RequestBody MovimentacaoRequest movimentacaoRequest) {
        return movimentacaoService.registrar(movimentacaoRequest);
    }

    @GetMapping("/produto/{produtoId}")
    public Page<MovimentacaoResponse> listarPorProduto(@PathVariable Long produtoId, Pageable pageable) {
        return movimentacaoService.listarPorProduto(produtoId, pageable);
    }
}
