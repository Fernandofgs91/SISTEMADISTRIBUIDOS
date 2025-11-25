package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.ContaPessimista;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.ContaPessimistaRepository;

@Service
public class ContaPessimistaService {
    
    private final ContaPessimistaRepository repository;
    
    public ContaPessimistaService(ContaPessimistaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public void deposita(String numero, Double valor) {
        ContaPessimista conta = repository.findByNumeroWithLock(numero);
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada");
        }
        conta.setSaldo(conta.getSaldo() + valor);
        repository.save(conta);
    }
    
    @Transactional
    public void saque(String numero, Double valor) {
        ContaPessimista conta = repository.findByNumeroWithLock(numero);
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada");
        }
        if (conta.getSaldo() < valor) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        repository.save(conta);
    }
}