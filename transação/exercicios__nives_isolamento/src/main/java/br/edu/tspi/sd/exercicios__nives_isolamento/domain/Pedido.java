package br.edu.tspi.sd.exercicios__nives_isolamento.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_PEDIDO")
@Getter
@Setter
@NoArgsConstructor
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_pedido")
    private Integer numero;

    @Column(name = "dat_pedido")
    private LocalDate data;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "pedido",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ItemPedido> itens = new ArrayList<>(); // Inicializar a lista

    // Métodos de conveniência para gerenciar a relação bidirecional
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
    }
}