package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.servico.ServicoRequest;
import dev.deodato.tcc.petshop.dto.servico.ServicoResponse;
import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public ServicoResponse criarServico(ServicoRequest request) {
        if(request.nome() == null) {
            throw new RuntimeException("O tipo de serviço deve não pode ser nulo");
        }

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
        servicoRepository.delete(servico);
    }

    private Servico buscarEntidadePorId(Long id) {
        return servicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Serviço não encontrado."));
    }
}
