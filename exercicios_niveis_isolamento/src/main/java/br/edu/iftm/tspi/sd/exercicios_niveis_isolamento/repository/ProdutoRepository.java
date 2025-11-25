package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository;

import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByEstoqueGreaterThan(Integer estoque);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}