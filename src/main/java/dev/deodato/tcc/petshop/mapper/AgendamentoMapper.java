package dev.deodato.tcc.petshop.mapper;

import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoRequest;
import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoResponse;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

import java.util.List;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoRequest req, Pet pet, List<Servico> servicos) {
        Agendamento agendamento = Agendamento.builder()
                .dataHora(req.dataHora())
                .observacao(req.observacao())
                .status(StatusAgendamento.AGENDADO)
                .pet(pet)
                .build();

        servicos.forEach(agendamento::adicionarItem);
        agendamento.recalcularTotal();
        return agendamento;
    }

    public static AgendamentoResponse toResponse(Agendamento agendamento) {
        List<AgendamentoResponse.ServicoItem> servicos = agendamento.itensOrdenados().stream()
                .map(i -> new AgendamentoResponse.ServicoItem(
                        i.getServico().getId(),
                        i.getServico().getNome(),
                        i.getPrecoUnitario()))
                .toList();

        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getTotal(),
                agendamento.getObservacao(),
                agendamento.getStatus(),
                agendamento.getPet().getNome(),
                agendamento.nomesDosServicos(),
                servicos
        );
    }
}
