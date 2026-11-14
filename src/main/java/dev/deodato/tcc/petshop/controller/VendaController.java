package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.venda.ProdutoVendaResponse;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;
import dev.deodato.tcc.petshop.service.VendaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
