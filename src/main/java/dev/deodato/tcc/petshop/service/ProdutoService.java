package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.produto.ProdutoRequest;
import dev.deodato.tcc.petshop.dto.produto.ProdutoResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Fornecedor;
import dev.deodato.tcc.petshop.model.Produto;
import dev.deodato.tcc.petshop.repository.MovimentacaoRepository;
import dev.deodato.tcc.petshop.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorService fornecedorService;
    private final MovimentacaoRepository movimentacaoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FornecedorService fornecedorService,  MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorService = fornecedorService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public ProdutoResponse cadastrar(ProdutoRequest produtoRequest) {
        Produto produto = produtoRequest.toEntity();
        Fornecedor fornecedor = fornecedorService.buscarEntidadePorId(produtoRequest.fornecedorId());
        produto.setFornecedor(fornecedor);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoSalvo);
    }

    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ProdutoResponse::fromEntity);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest produtoRequest) {
        Produto produto = buscarEntidadePorId(id);
        produtoRequest.preencher(produto);
        produto.setId(id);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoAtualizado);
    }

    public void excluir(Long id) {
        Produto produto = buscarEntidadePorId(id);

        if(movimentacaoRepository.existsByProdutoId(produto.getId())) {
            throw new PetShopException("Não é possível excluir: existem movimentaçõs para este produto.");
        }
        produtoRepository.delete(produto);
    }

    public Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new PetShopException("Produto não encontrado."));
    }

    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.fromEntity(buscarEntidadePorId(id));
    }
}
