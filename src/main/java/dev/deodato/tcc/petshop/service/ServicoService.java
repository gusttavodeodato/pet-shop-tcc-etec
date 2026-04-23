package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.model.Servico;
import dev.deodato.tcc.petshop.repository.ServicoRepository;
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

    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    public Optional<Servico> buscarServicoId(@PathVariable Long id) {
        return servicoRepository.findById(id);
    }

    public Servico salvarServico(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico atualizarServico(Servico servico) {
        return servicoRepository.save(servico);
    }

    public void deletarServico(@PathVariable Long id) {
        servicoRepository.deleteById(id);
    }
}
