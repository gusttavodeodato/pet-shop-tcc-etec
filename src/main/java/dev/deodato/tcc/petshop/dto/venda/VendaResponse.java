package dev.deodato.tcc.petshop.dto.venda;

import dev.deodato.tcc.petshop.model.Venda;
import dev.deodato.tcc.petshop.model.enums.FormaDePagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
        Long id,
        LocalDateTime dataHora,
        FormaDePagamento formaPagamento,
        BigDecimal total,
        String cliente,
        List<Item> itens
) {

    public record Item(Long produtoId, String produto, int quantidade, BigDecimal precoUnitario, BigDecimal subTotal) {}

    public static VendaResponse fromEntity(Venda venda) {
        List<Item> itens = venda.getItens().stream()
                .map(i -> new Item(
                        i.getProduto().getId(),
                        i.getProduto().getNome(),
                        i.getQuantidade(),
                        i.getPrecoUnitario(),
                        i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade()))))
                .toList();

        String cliente = venda.getTutor() != null ? venda.getTutor().getNome() : "Balção";

        return new VendaResponse(venda.getId(), venda.getDataHora(), venda.getFormaPagamento(), venda.getTotal(), cliente, itens);
    }
}
