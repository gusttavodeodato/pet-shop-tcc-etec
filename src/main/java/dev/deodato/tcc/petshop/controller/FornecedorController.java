package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.fornecedor.FornecedorRequest;
import dev.deodato.tcc.petshop.dto.fornecedor.FornecedorResponse;
import dev.deodato.tcc.petshop.service.FornecedorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/fornecedores")
public class FornecedorController {

    private FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FornecedorResponse cadastrar(@RequestBody FornecedorRequest fornecedorRequest) {
        return fornecedorService.cadastrar(fornecedorRequest);
    }

    @GetMapping
    public Page<FornecedorResponse> listar(Pageable pageable) {
        return fornecedorService.listar(pageable);
    }

    @GetMapping("/{id}")
    public FornecedorResponse buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public FornecedorResponse atualizar(@PathVariable Long id, @RequestBody FornecedorRequest fornecedorRequest) {
        return fornecedorService.atualizar(id, fornecedorRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        fornecedorService.excluir(id);
    }
}
