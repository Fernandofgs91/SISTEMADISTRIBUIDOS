package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository;

import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {}
