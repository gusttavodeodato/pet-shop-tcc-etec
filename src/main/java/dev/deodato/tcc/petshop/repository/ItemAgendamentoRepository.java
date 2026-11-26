package dev.deodato.tcc.petshop.repository;

import dev.deodato.tcc.petshop.model.ItemAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAgendamentoRepository  extends JpaRepository<ItemAgendamento, Long> {
    boolean existsByServicoId(Long servicoId);
}
