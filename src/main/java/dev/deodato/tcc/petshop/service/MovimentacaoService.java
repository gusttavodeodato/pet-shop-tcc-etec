package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.movimentacao.MovimentacaoRequest;
import dev.deodato.tcc.petshop.dto.movimentacao.MovimentacaoResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Movimentacao;
import dev.deodato.tcc.petshop.model.Produto;
import dev.deodato.tcc.petshop.model.enums.TipoOperacao;
import dev.deodato.tcc.petshop.repository.MovimentacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoService produtoService;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ProdutoService produtoService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest request) {

        Produto produto = produtoService.buscarEntidadePorId(request.produtoId());

        Integer saldoAtual = produto.getSaldo() == null ? 0 : produto.getSaldo();

        Integer novoSaldo = calculaNovoSaldo(saldoAtual, request.tipo(), request.quantidade());

        if(novoSaldo < 0) {
            throw new PetShopException("Saldo insuficiente");
        }

        produto.setSaldo(novoSaldo);

        Movimentacao entity = request.toEntity();
        entity.setProduto(produto);
        entity = movimentacaoRepository.save(entity);
        return MovimentacaoResponse.fromEntity(entity);
    }

    @Transactional
    public Page<MovimentacaoResponse> listarPorProduto(Long produtoId, Pageable pageable) {
        produtoService.buscarEntidadePorId(produtoId);
        return movimentacaoRepository.findByProdutoId(produtoId, pageable).map(MovimentacaoResponse::fromEntity);
    }

    private Integer calculaNovoSaldo(Integer saldoAtual, TipoOperacao tipo, Integer quantidade) {
        if(tipo == TipoOperacao.ENTRADA) {
            Integer novoSaldo = saldoAtual + quantidade;
            return novoSaldo;
        }

        if(tipo == TipoOperacao.SAIDA) {
            Integer novoSaldo = saldoAtual - quantidade;
            return novoSaldo;
        }

        throw new PetShopException("Tipo de operação não suportada.");
    }

    public Movimentacao buscarEntidadePorId(Long id) {
        return movimentacaoRepository.findById(id)
                .orElseThrow(() -> new PetShopException("Operação não encontrada."));
    }
}
