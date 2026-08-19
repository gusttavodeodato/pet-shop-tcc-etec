package dev.deodato.tcc.petshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer quantMax;
    private Integer quantMinima;

    private Integer saldo;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @OneToMany
    @JoinColumn(name = "produto_id", nullable = false, unique = true)
    private List<Movimentacao> movimentacao = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
