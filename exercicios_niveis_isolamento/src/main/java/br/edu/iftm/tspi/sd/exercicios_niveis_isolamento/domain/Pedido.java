package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
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
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pedido", 
               cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;
}