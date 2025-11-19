package br.edu.tspi.sd.exercicios__nives_isolamento.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_PRODUTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_produto")
    private Integer id;

    @Column(name = "nom_produto")
    private String nome;

    @Column(name = "vlr_produto")
    private Double valor;

    @Column(name = "qtd_estoque")
    private Integer estoque;
}
