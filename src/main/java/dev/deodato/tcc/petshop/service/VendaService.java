package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.produto.ProdutoResponse;
import dev.deodato.tcc.petshop.dto.venda.ProdutoVendaResponse;
import dev.deodato.tcc.petshop.model.Produto;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;
import dev.deodato.tcc.petshop.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VendaService {

    private final ProdutoRepository produtos;

    public VendaService(ProdutoRepository produtos) {
        this.produtos = produtos;
    }

    public List<ProdutoVendaResponse> listarProdutos(CategoriaProduto categoria, String busca) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();

        return produtos.findByRevendaTrueOrderByNomeAsc().stream()
                .filter(p -> categoria == null || p.getCategoria() == categoria)
                .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(termo))
                .map(this::paraResponse)
                .toList();
    }

    private ProdutoVendaResponse paraResponse(Produto p) {
        int saldo = p.getSaldo() == null ? 0 : p.getSaldo();
        Integer minimo = p.getQuantMinima();

        ProdutoVendaResponse.Situacao situacao;

        if(saldo <= 0) {
            situacao = ProdutoVendaResponse.Situacao.ESGOTADO;
        } else if (minimo != null && saldo <= minimo) {
            situacao = ProdutoVendaResponse.Situacao.ESTOQUE_BAIXO;
        } else {
            situacao = ProdutoVendaResponse.Situacao.NORMAL;
        }

        return new ProdutoVendaResponse(p.getId(), p.getNome(), p.getPreco(), p.getCategoria(), saldo, situacao);
    }
}
