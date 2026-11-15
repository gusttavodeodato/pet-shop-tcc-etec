package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.dto.viacep.EnderecoResponse;
import dev.deodato.tcc.petshop.service.CepService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cep")
public class CepController {

    private final CepService service;

    public CepController(CepService service) {
        this.service = service;
    }

    @GetMapping("/{cep}")
    public EnderecoResponse buscar(@PathVariable String cep){
        return service.buscar(cep);
    }
}
