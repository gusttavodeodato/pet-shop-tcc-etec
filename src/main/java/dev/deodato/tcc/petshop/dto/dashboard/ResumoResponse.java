package dev.deodato.tcc.petshop.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record ResumoResponse(
        Contagem clientes,
        Contagem pets,
        Hoje hoje,
        BigDecimal faturamentoMes,
        List<EstoqueBaixo> estoqueBaixo
) {
    public record Contagem(Long total, Long novoMes) {}

    public record Hoje(long total, long agendados, long confirmados, long emAndamento, long concluidos, long cancelados) {}

    public record EstoqueBaixo(String produto, int saldo) {}
}
