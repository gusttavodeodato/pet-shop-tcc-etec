package dev.deodato.tcc.petshop.dto.relatorio;

import dev.deodato.tcc.petshop.dto.dashboard.ResumoResponse;

public record ClientesPetsResponse(Contagem clientes, Contagem pets) {
    public record Contagem(long total, long novos) {}
}
