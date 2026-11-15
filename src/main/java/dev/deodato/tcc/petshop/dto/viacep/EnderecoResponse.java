package dev.deodato.tcc.petshop.dto.viacep;

public record EnderecoResponse(
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf
) {
}
