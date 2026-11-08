package dev.deodato.tcc.petshop.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RelatorioResponse(
        Long totalServicos,
        BigDecimal receitaTotal,
        BigDecimal ticketMedio,
        List<ServicoPorTipo> servicoPorTipo,
        List<ReceitaPorDia> receitaPorDia
) {
    public record ServicoPorTipo(String tipo, long quantidade, BigDecimal receita) {}

    public record ReceitaPorDia(LocalDate data, BigDecimal total) {}
}
