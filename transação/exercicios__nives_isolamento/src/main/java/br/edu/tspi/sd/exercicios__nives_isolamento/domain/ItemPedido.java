package br.edu.tspi.sd.exercicios__nives_isolamento.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_ITEM_PEDIDO")
@Getter
@Setter
@NoArgsConstructor
public class ItemPedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_item")
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "num_pedido")
    private Pedido pedido; // Removido @JsonIgnore

    @ManyToOne
    @JoinColumn(name = "cod_produto")
    private Produto produto;

    @Column(name = "qtd_compra")
    private Integer quantidade;
}