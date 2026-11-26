package dev.deodato.tcc.petshop.model;

import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_agendamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    private BigDecimal total;

    private String observacao;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    private LocalDateTime criadoEm;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;


    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @BatchSize(size = 50)
    @OrderBy("id ASC")
    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemAgendamento> itens = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public void adicionarItem(Servico servico) {
        ItemAgendamento item = new ItemAgendamento();
        item.setAgendamento(this);
        item.setServico(servico);
        item.setPrecoUnitario(servico.getPreco() != null ? servico.getPreco() : BigDecimal.ZERO);
        itens.add(item);
    }

    public void recalcularTotal() {
        total = itens.stream()
                .map(ItemAgendamento::getPrecoUnitario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ItemAgendamento> itensOrdenados() {
        return itens.stream()
                .sorted(Comparator.comparing(ItemAgendamento::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public String nomesDosServicos() {
        return itensOrdenados().stream()
                .map(i -> i.getServico().getNome())
                .collect(Collectors.joining(" + "));
    }
}
