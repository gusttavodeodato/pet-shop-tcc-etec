package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoRequest;
import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoResponse;
import dev.deodato.tcc.petshop.mapper.AgendamentoMapper;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PetService petService;
    private final ServicoService servicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PetService petService, ServicoService servicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.petService = petService;
        this.servicoService = servicoService;
    }

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest agendamento) {

        Pet pet = petService.buscarEntidadePorId(agendamento.petID());
        Servico servico = servicoService.buscarEntidadePorId(agendamento.servicoID());

        if(agendamentoRepository.existsByPetAndDataHora(pet, agendamento.dataHora())) {
            throw new IllegalArgumentException("Já existe pet nesse horário agendado.");
        }

        Agendamento entity = AgendamentoMapper.toEntity(agendamento, pet, servico);
        entity = agendamentoRepository.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id,  AgendamentoRequest request) {
        Agendamento agendamento = buscarEntidadePorId(id);

        Pet pet = petService.buscarEntidadePorId(request.petID());
        Servico servico = servicoService.buscarEntidadePorId(request.servicoID());

        agendamento.setDataHora(request.dataHora());
        agendamento.setObservacao(request.observacao());
        agendamento.setServico(servico);
        agendamento.setPet(pet);


        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse concluido(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        return AgendamentoMapper.toResponse(agendamento);
    }

    public Agendamento buscarEntidadePorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));
    }
}
