package dev.deodato.tcc.petshop.model;

import dev.deodato.tcc.petshop.model.enums.TipoOperacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_estoque")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;
    private Integer quantidadeMinima;


    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @OneToOne
    @JoinColumn(name = "produto_id", nullable = false, unique = true)
    private Produto produto;

    @PrePersist
    public void prePersist() {criadoEm =  LocalDateTime.now();}

    @PreUpdate
    public void preUpdate() {atualizadoEm =  LocalDateTime.now();}
}
