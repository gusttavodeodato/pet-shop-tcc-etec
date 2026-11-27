package dev.deodato.tcc.petshop.dto.agendamento;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponse(
        Long id,
        LocalDateTime dataHora,
        BigDecimal total,
        String observacao,
        StatusAgendamento status,
        String pet,
        String servico,
        List<ServicoItem> servicos
) {
    public record ServicoItem(Long id, String nome, BigDecimal preco) {}
}
