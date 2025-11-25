package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "tb_conta_otimista")
@Getter
@Setter
@NoArgsConstructor
public class ContaOtimista implements Serializable {
    
    @Id
    @Column(name = "num_conta")
    private String numero;
    
    @Column(name = "vir_saldo")
    private Double saldo;
    
    @Version
    @Column(name = "cod_versao")
    private Integer versao;
}