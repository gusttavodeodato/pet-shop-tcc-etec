package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.fornecedor.FornecedorRequest;
import dev.deodato.tcc.petshop.dto.fornecedor.FornecedorResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Fornecedor;
import dev.deodato.tcc.petshop.repository.FornecedorRepository;
import dev.deodato.tcc.petshop.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public FornecedorService(FornecedorRepository fornecedorRepositoy, ProdutoRepository produtoRepository) {
        this.fornecedorRepository = fornecedorRepositoy;
        this.produtoRepository = produtoRepository;
    }

    public FornecedorResponse cadastrar(FornecedorRequest fornecedorRequest) {
        Fornecedor fornecedor = fornecedorRequest.toEntity();
        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
        return FornecedorResponse.fromEntity(fornecedorSalvo);
    }

    public Page<FornecedorResponse> listar(Pageable pageable) {
        return fornecedorRepository.findAll(pageable).map(FornecedorResponse::fromEntity);
    }

    public FornecedorResponse buscarPorId(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        return FornecedorResponse.fromEntity(fornecedor);
    }

    public FornecedorResponse atualizar(Long id, FornecedorRequest fornecedorRequest) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        fornecedorRequest.preencher(fornecedor);
        Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedor);
        return FornecedorResponse.fromEntity(fornecedorAtualizado);
    }

    public void excluir(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);

        if(produtoRepository.existsByFornecedorId(id)) {
            throw new PetShopException("Não é possível excluir: existem produtos vinculados a este fornecedor.");
        }
        fornecedorRepository.delete(fornecedor);
    }

    public Fornecedor buscarEntidadePorId(Long id) {
        return fornecedorRepository.findById(id).orElseThrow(() -> new PetShopException("Fornecedor não encontrado."));
    }
}
