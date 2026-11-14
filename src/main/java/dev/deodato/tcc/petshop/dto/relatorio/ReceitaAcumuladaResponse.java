package dev.deodato.tcc.petshop.dto.relatorio;

import java.math.BigDecimal;
import java.util.List;

public record ReceitaAcumuladaResponse(List<Ponto> mesSelecionado, List<Ponto> mesAnterior) {
    public record Ponto(int dia, BigDecimal acumulado) {}
}
