package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.venda.ProdutoVendaResponse;
import dev.deodato.tcc.petshop.dto.venda.VendaRequest;
import dev.deodato.tcc.petshop.dto.venda.VendaResponse;
import dev.deodato.tcc.petshop.dto.venda.VendasHojeResponse;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;
import dev.deodato.tcc.petshop.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vendas")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping("/produtos")
    public List<ProdutoVendaResponse> produtos(
            @RequestParam(required = false)CategoriaProduto categoria,
            @RequestParam(required = false) String busca) {
        return service.listarProdutos(categoria, busca);
    }

    @GetMapping("/hoje")
    public VendasHojeResponse hoje() {
        return service.vendasHoje();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaResponse registrar(@RequestBody @Valid VendaRequest request) {
        return service.registrar(request);
    }
}
