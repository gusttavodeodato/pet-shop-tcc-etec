package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.servico.ServicoRequest;
import dev.deodato.tcc.petshop.dto.servico.ServicoResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.repository.AgendamentoRepository;
import dev.deodato.tcc.petshop.repository.MovimentacaoRepository;
import dev.deodato.tcc.petshop.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public ServicoService(ServicoRepository servicoRepository, AgendamentoRepository agendamentoRepository ) {
        this.servicoRepository = servicoRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    public ServicoResponse criarServico(ServicoRequest request) {

        Servico servico = request.toEntity();
        Servico servicoSalvo = servicoRepository.save(servico);
        return ServicoResponse.fromEntity(servicoSalvo);
    }

    public Page<ServicoResponse> listarServicos(Pageable pageable) {
        return servicoRepository.findAll(pageable).map(ServicoResponse::fromEntity);
    }

    public ServicoResponse buscarServicoPorId(Long id) {
       Servico servico = buscarEntidadePorId(id);
       return ServicoResponse.fromEntity(servico);
    }

    public ServicoResponse atualizarServicoPorId(Long id, ServicoRequest request) {
        Servico servico = buscarEntidadePorId(id);
        request.preencher(servico);
        Servico servicoSalvo = servicoRepository.save(servico);
        return ServicoResponse.fromEntity(servicoSalvo);
    }

    public void excluirServico(Long id) {
        Servico servico = buscarEntidadePorId(id);

        if(agendamentoRepository.existsByServicoId(servico.getId())) {
            throw new PetShopException("Não é possível excluir: existe um serviço agendado.");
        }
        servicoRepository.delete(servico);
    }

    public Servico buscarEntidadePorId(Long id) {
        return servicoRepository.findById(id).orElseThrow(() -> new PetShopException("Serviço não encontrado."));
    }
}
