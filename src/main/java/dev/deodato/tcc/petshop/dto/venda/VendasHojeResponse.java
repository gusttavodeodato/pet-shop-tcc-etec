package dev.deodato.tcc.petshop.dto.venda;

import dev.deodato.tcc.petshop.model.enums.FormaDePagamento;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public record VendasHojeResponse(
        long quantidade,
        BigDecimal totalVendido,
        List<VendaResumo> vendas
) {
    public record VendaResumo(
            Long id,
            LocalTime hora,
            String cliente,
            FormaDePagamento formaPagamento,
            BigDecimal total,
            List<ItemResumo> itens
    ) {}

    public record ItemResumo(String produto, int quantidade) {}
}
