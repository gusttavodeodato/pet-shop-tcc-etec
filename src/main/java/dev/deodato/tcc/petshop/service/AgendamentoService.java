package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoRequest;
import dev.deodato.tcc.petshop.dto.agendamento.AgendamentoResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.mapper.AgendamentoMapper;
import dev.deodato.tcc.petshop.model.Agendamento;
import dev.deodato.tcc.petshop.model.Pet;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.model.enums.StatusAgendamento;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static dev.deodato.tcc.petshop.service.DashboardService.BRASIL;

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
        if(agendamento.dataHora().isBefore(LocalDateTime.now(BRASIL))) {
            throw new PetShopException("Não é possível agendar no passado.");
        }

        Pet pet = petService.buscarEntidadePorId(agendamento.petID());
        Servico servico = servicoService.buscarEntidadePorId(agendamento.servicoID());

        if(agendamentoRepository.existsByPetAndDataHora(pet, agendamento.dataHora())) {
            throw new PetShopException("Já existe pet nesse horário agendado.");
        }

        Agendamento entity = AgendamentoMapper.toEntity(agendamento, pet, servico);
        entity = agendamentoRepository.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id,  AgendamentoRequest request) {
        Agendamento agendamento = buscarEntidadePorId(id);
        validarEmAberto(agendamento);

        Pet pet = petService.buscarEntidadePorId(request.petID());
        Servico servico = servicoService.buscarEntidadePorId(request.servicoID());

        boolean mudouHorario = !request.dataHora().equals(agendamento.getDataHora());
        if(mudouHorario && request.dataHora().isBefore(LocalDateTime.now(BRASIL))) {
            throw new PetShopException("Não é possível agendar no passado.");
        }

        if(agendamentoRepository.existsByPetAndDataHoraAndIdNot(pet, request.dataHora(), id)) {
            throw new PetShopException("Já existe pet nesse horário agendado.");
        }

        if(!agendamento.getServico().getId().equals(servico.getId())) {
            agendamento.setTotal(servico.getPreco());
        }

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
        validarEmAberto(agendamento);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse concluido(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        validarEmAberto(agendamento);
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public Page<AgendamentoResponse> listar(StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim,  Pageable pageable) {
        LocalDateTime de = inicio != null ? inicio : LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime ate = fim != null ? fim : LocalDateTime.of(2100, 1, 1, 0, 0);

        Page<Agendamento> pagina = status != null ? agendamentoRepository.findByStatusAndDataHoraBetween(status, de, ate, pageable)
                : agendamentoRepository.findByDataHoraBetween(de, ate, pageable);

        return pagina.map(AgendamentoMapper::toResponse);
    }

    @Transactional
    public void excluir(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamentoRepository.delete(agendamento);
    }

    @Transactional
    public AgendamentoResponse reabrir(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);

        if(agendamento.getStatus() != StatusAgendamento.CONCLUIDO) {
            throw new PetShopException("Só é possível reabrir um atendimento concluído.");
        }

        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamentoRepository.save(agendamento);
        return AgendamentoMapper.toResponse(agendamento);
    }

    public void validarEmAberto(Agendamento agendamento) {
        if(agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new PetShopException("Este atendimento já foi concluído.");
        }
        if(agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new PetShopException("Este atendimento já foi cancelado.");
        }
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        return AgendamentoMapper.toResponse(agendamento);
    }

    public Agendamento buscarEntidadePorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new PetShopException("Agendamento não encontrado."));
    }
}
