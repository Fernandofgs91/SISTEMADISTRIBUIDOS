package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service;

import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.Produto;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }
    
    public List<Produto> buscarProdutosComEstoque() {
        return produtoRepository.findByEstoqueGreaterThan(0);
    }
    
    public List<Produto> buscarProdutosPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Produto buscarProdutoPorId(Integer id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}