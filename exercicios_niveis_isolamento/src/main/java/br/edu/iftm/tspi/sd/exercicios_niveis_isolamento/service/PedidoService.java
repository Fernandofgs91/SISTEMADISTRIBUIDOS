package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.ItemPedido;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.Pedido;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.EstoqueRepository;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.PedidoRepository;

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