package dev.deodato.tcc.petshop.dto.relatorio;

import java.math.BigDecimal;

public record ServicoPopular (String servico, long quantidade, BigDecimal receita) {}
