package dev.deodato.tcc.petshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_tutores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private String email;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void criadoEm() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void atualizadoEm() {
        atualizadoEm = LocalDateTime.now();
    }

    @ManyToMany(mappedBy = "tutores")
    @JsonIgnoreProperties("tutores")
    private List<Pet> pets;
}
