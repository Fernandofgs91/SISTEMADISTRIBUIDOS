package br.edu.tspi.sd.exercicios__nives_isolamento.repository; // Nome corrigido

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.tspi.sd.exercicios__nives_isolamento.domain.Pedido; // Nome corrigido

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}