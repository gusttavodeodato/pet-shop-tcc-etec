package dev.deodato.tcc.petshop.model;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agendamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    private LocalDateTime criadoEm;

    @PrePersist
    public void criadoEm() {
        criadoEm = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;


    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

}
