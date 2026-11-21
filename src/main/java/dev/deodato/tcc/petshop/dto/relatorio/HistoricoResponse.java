package dev.deodato.tcc.petshop.dto.relatorio;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoResponse(
        Resumo resumo,
        long total,
        int pagina,
        int tamanho,
        List<Linha> linhas
) {

    public record Resumo(long servicoMes, long concluidos, long cancelados) {}

    public record Linha(
            Long id,
            String pet,
            String tutor,
            String servico,
            LocalDateTime dataHora,
            String observacao,
            StatusAgendamento status,
            BigDecimal valor
    ) {}
}
