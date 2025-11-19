package br.edu.tspi.sd.exercicios__nives_isolamento.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.tspi.sd.exercicios__nives_isolamento.domain.ItemPedido;
import br.edu.tspi.sd.exercicios__nives_isolamento.domain.Pedido;
import br.edu.tspi.sd.exercicios__nives_isolamento.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            item.setPedido(pedido);
        }
        Pedido pedidoSalvo = pedidoService.salvarPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }
}