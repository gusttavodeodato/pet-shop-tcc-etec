package dev.deodato.tcc.petshop.model;

import dev.deodato.tcc.petshop.model.enums.TipoOperacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_movimentacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoOperacao tipo;

    private Integer quantidade;

    private LocalDateTime dataMovimentacao;

    @PrePersist
    public void prePersist() {
        dataMovimentacao = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

}
