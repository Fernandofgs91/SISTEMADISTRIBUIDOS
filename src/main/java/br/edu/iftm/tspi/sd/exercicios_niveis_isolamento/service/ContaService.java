package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service;

import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.ContaOtimista;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.domain.ContaPessimista;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.ContaOtimistaRepository;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.repository.ContaPessimistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaService {

    private final ContaOtimistaRepository otimistaRepo;
    private final ContaPessimistaRepository pessimistaRepo;

    public ContaService(
            ContaOtimistaRepository otimistaRepo,
            ContaPessimistaRepository pessimistaRepo
    ) {
        this.otimistaRepo = otimistaRepo;
        this.pessimistaRepo = pessimistaRepo;
    }

    // ===============================================================
    //                         LOCK OTIMISTA
    // ===============================================================

    @Transactional
    public ContaOtimista depositarOtimista(String numConta, Double valor, long sleepMs)
            throws InterruptedException {

        ContaOtimista conta = otimistaRepo.findById(numConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada (otimista): " + numConta));

        if (sleepMs > 0) Thread.sleep(sleepMs);

        conta.setSaldo(conta.getSaldo() + valor);
        return otimistaRepo.save(conta);
    }

    @Transactional
    public ContaOtimista retirarOtimista(String numConta, Double valor, long sleepMs)
            throws InterruptedException {

        ContaOtimista conta = otimistaRepo.findById(numConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada (otimista): " + numConta));

        if (sleepMs > 0) Thread.sleep(sleepMs);

        if (conta.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo() - valor);
        return otimistaRepo.save(conta);
    }

    // ===============================================================
    //                         LOCK PESSIMISTA
    // ===============================================================

    @Transactional
    public ContaPessimista depositarPessimista(String numConta, Double valor, long sleepMs)
            throws InterruptedException {

        ContaPessimista conta = pessimistaRepo.findByNumeroWithLock(numConta);

        if (conta == null) {
            throw new RuntimeException("Conta não encontrada (pessimista): " + numConta);
        }

        if (sleepMs > 0) Thread.sleep(sleepMs);

        conta.setSaldo(conta.getSaldo() + valor);
        return pessimistaRepo.save(conta);
    }

    @Transactional
    public ContaPessimista retirarPessimista(String numConta, Double valor, long sleepMs)
            throws InterruptedException {

        ContaPessimista conta = pessimistaRepo.findByNumeroWithLock(numConta);

        if (conta == null) {
            throw new RuntimeException("Conta não encontrada (pessimista): " + numConta);
        }

        if (sleepMs > 0) Thread.sleep(sleepMs);

        if (conta.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo() - valor);
        return pessimistaRepo.save(conta);
    }
}