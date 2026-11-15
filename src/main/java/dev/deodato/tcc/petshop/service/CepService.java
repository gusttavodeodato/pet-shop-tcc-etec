package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.viacep.EnderecoResponse;
import dev.deodato.tcc.petshop.dto.viacep.ViaCepResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class CepService {

    private final RestClient viaCep;

    public CepService(RestClient viaCep) {
        this.viaCep = viaCep;
    }

    public EnderecoResponse buscar(String cepInformado) {
        String cep = cepInformado == null ? "" : cepInformado.replaceAll("\\D", "");
        if(cep.length() != 8) {
            throw new PetShopException("CEP inválido. Informe os 8 números.");
        }

        ViaCepResponse resposta;
        try {
            resposta = viaCep.get()
                    .uri("/{cep}/json/", cep)
                    .retrieve()
                    .body(ViaCepResponse.class);
        } catch (RestClientException e) {
            throw new PetShopException("Não foi possível consultar o CEP agora. Tente novamente.");
        }

        if(resposta == null || Boolean.TRUE.equals(resposta.erro())) {
            throw new PetShopException("CEP não encontrado.");
        }

        return new EnderecoResponse(cep, resposta.logradouro(), resposta.bairro(), resposta.localidade(), resposta.uf());
    }
}
