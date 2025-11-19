package br.edu.tspi.sd.exercicios__nives_isolamento.service;

import org.springframework.stereotype.Service;

import br.edu.tspi.sd.exercicios__nives_isolamento.domain.ItemPedido;
import br.edu.tspi.sd.exercicios__nives_isolamento.domain.Pedido;
import br.edu.tspi.sd.exercicios__nives_isolamento.repository.EstoqueRepository;
import br.edu.tspi.sd.exercicios__nives_isolamento.     repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final EstoqueRepository estoqueRepository;
    
    public PedidoService(PedidoRepository pedidoRepository, EstoqueRepository estoqueRepository) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public Pedido salvarPedido(Pedido pedido) {
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        for (ItemPedido item : pedido.getItens()) {
            estoqueRepository.atualizaEstoque(item.getProduto().getId(), item.getQuantidade());
        }
        
        return pedidoSalvo;
    }
}