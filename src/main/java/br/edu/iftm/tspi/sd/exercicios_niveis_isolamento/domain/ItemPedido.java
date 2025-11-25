package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "tb_item_pedido")
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
    @JsonIgnore
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "cod_produto")
    private Produto produto;
    
    @Column(name = "qtd_compra")
    private Integer quantidade;
}