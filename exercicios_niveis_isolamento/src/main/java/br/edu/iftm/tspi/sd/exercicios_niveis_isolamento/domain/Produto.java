package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "tb_produto")
@Getter
@Setter
@NoArgsConstructor
public class Produto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_produto")
    private Integer id;
    
    @Column(name = "nom_produto")
    private String nome;
    
    @Column(name = "vir_produto")
    private Double valor;
    
    @Column(name = "qtd_estoque")
    private Integer estoque;
}