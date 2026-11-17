package dev.deodato.tcc.petshop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_tokens_recuperacao")
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataExpiracao;

    private boolean usado;

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();
    }

}
