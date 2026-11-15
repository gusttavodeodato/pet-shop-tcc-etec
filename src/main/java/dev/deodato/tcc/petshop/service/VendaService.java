package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.dto.produto.ProdutoResponse;
import dev.deodato.tcc.petshop.dto.venda.ProdutoVendaResponse;
import dev.deodato.tcc.petshop.dto.venda.VendaRequest;
import dev.deodato.tcc.petshop.dto.venda.VendaResponse;
import dev.deodato.tcc.petshop.dto.venda.VendasHojeResponse;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.*;
import dev.deodato.tcc.petshop.model.enums.CategoriaProduto;
import dev.deodato.tcc.petshop.model.enums.TipoOperacao;
import dev.deodato.tcc.petshop.repository.MovimentacaoRepository;
import dev.deodato.tcc.petshop.repository.ProdutoRepository;
import dev.deodato.tcc.petshop.repository.TutorRepository;
import dev.deodato.tcc.petshop.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VendaService {
    private static final ZoneId BRASIL = ZoneId.of("America/Sao_Paulo");

    private final ProdutoRepository produtos;
    private final VendaRepository vendas;
    private final TutorRepository tutores;
    private final MovimentacaoRepository movimentacoes;

    public VendaService(ProdutoRepository produtos, VendaRepository vendas, TutorRepository tutores, MovimentacaoRepository movimentacoes) {
        this.produtos = produtos;
        this.vendas = vendas;
        this.tutores = tutores;
        this.movimentacoes = movimentacoes;
    }

    public List<ProdutoVendaResponse> listarProdutos(CategoriaProduto categoria, String busca) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();

        return produtos.findByRevendaTrueOrderByNomeAsc().stream()
                .filter(p -> categoria == null || p.getCategoria() == categoria)
                .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(termo))
                .map(this::paraResponse)
                .toList();
    }

    @Transactional
    public VendaResponse registrar(VendaRequest request) {
        Map<Long, Integer> quantidades = new LinkedHashMap<>();
        for(VendaRequest.ItemRequest item : request.itens()) {
            quantidades.merge(item.produtoId(), item.quantidade(), Integer::sum);
        }

        Map<Long, Produto> porId = produtos.buscarParaVenda(quantidades.keySet()).stream()
                .collect(Collectors.toMap(Produto::getId, p -> p));

        Tutor tutor = null;
        if(request.tutorId() != null) {
            tutor = tutores.findById(request.tutorId())
                    .orElseThrow(() -> new PetShopException("Cliente não encontrado."));
        }

        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now(BRASIL));
        venda.setFormaPagamento(request.formaPagamento());
        venda.setTutor(tutor);

        BigDecimal total = BigDecimal.ZERO;

        for(Map.Entry<Long, Integer> entrada : quantidades.entrySet()) {
            Produto produto = porId.get(entrada.getKey());
            if(produto == null) {
                throw new PetShopException("Produto não encontrado: " + entrada.getKey());
            }

            if(!produto.isRevenda()) {
                throw new PetShopException("O produto " + produto.getNome() + " não está disponível para venda.");
            }

            int quantidade = entrada.getValue();
            int saldo = produto.getSaldo() == null ? 0 : produto.getSaldo();
            if(quantidade > saldo) {
                throw new PetShopException("Saldo insuficiente para " + produto.getNome() + " (disponível: " + saldo + ").");
            }

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPreco());
            item.setCustoUnitario(produto.getPrecoCusto() != null ? produto.getPrecoCusto() : BigDecimal.ZERO);
            venda.adicionarItem(item);

            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
            produto.setSaldo(saldo - quantidade);
        }

        venda.setTotal(total);
        venda = vendas.save(venda);

        for(ItemVenda item : venda.getItens()) {
            Movimentacao saida = new Movimentacao();
                saida.setTipo(TipoOperacao.SAIDA);
                saida.setQuantidade(item.getQuantidade());
                saida.setProduto(item.getProduto());
                saida.setVenda(venda);
                movimentacoes.save(saida);
            }

        return VendaResponse.fromEntity(venda);
    }

    public VendasHojeResponse vendasHoje() {
        LocalDate hoje = LocalDate.now(BRASIL);

        List<Venda> doDia = vendas.findByDataHoraBetweenOrderByDataHoraDesc(hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX));

        BigDecimal total = doDia.stream()
                .map(Venda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<VendasHojeResponse.VendaResumo> lista = doDia.stream()
                .map(this::paraResumo)
                .toList();

        return new VendasHojeResponse(doDia.size(), total, lista);
    }

    private VendasHojeResponse.VendaResumo paraResumo(Venda v) {
        List<VendasHojeResponse.ItemResumo> itens = v.getItens().stream()
                .map((ItemVenda i) -> new VendasHojeResponse.ItemResumo(i.getProduto().getNome(), i.getQuantidade()))
                .toList();

        String cliente = v.getTutor() != null ? v.getTutor().getNome() : "Balcão";

        return new VendasHojeResponse.VendaResumo(v.getId(), v.getDataHora().toLocalTime(), cliente, v.getFormaPagamento(), v.getTotal(), itens);
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
