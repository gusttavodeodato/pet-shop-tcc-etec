package dev.deodato.tcc.petshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name =  "tb_metas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ano", "mes"}))
public class MetaMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    public MetaMensal(int ano, int mes, BigDecimal valor) {
        this.ano = ano;
        this.mes = mes;
        this.valor = valor;
    }
}
