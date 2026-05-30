package dev.deodato.tcc.petshop.mapper;

import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoRequest;
import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoResponse;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoRequest req, Pet pet, Servico servico) {
        return Agendamento.builder()
                .dataHora(req.dataHora())
                .observacao(req.observacao())
                .status(StatusAgendamento.AGENDADO)
                .pet(pet)
                .servico(servico)
                .build();
    }

    public static AgendamentoResponse toResponse(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getObservacao(),
                agendamento.getStatus(),
                agendamento.getPet().getNome(),
                agendamento.getServico().getNome()
        );
    }
}
