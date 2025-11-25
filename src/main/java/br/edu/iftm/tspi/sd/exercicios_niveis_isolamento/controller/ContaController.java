package br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.dto.ContaRequest;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service.ContaOtimistaService;
import br.edu.iftm.tspi.sd.exercicios_niveis_isolamento.service.ContaPessimistaService;

@Controller
@RequestMapping("/conta")
public class ContaController {
    
    private final ContaOtimistaService contaOtimistaService;
    private final ContaPessimistaService contaPessimistaService;
    
    public ContaController(ContaOtimistaService contaOtimistaService, 
                          ContaPessimistaService contaPessimistaService) {
        this.contaOtimistaService = contaOtimistaService;
        this.contaPessimistaService = contaPessimistaService;
    }
    
    @PostMapping("/{numero}/depositaOtimista")
    public ResponseEntity<String> depositaOtimista(@PathVariable String numero, 
                                                   @RequestBody ContaRequest contaRequest) {
        contaOtimistaService.deposita(numero, contaRequest.getValor());
        return ResponseEntity.ok("Depósito Otimista realizado com sucesso.");
    }
    
    @PostMapping("/{numero}/saqueOtimista")
    public ResponseEntity<String> saqueOtimista(@PathVariable String numero, 
                                                @RequestBody ContaRequest contaRequest) {
        contaOtimistaService.saque(numero, contaRequest.getValor());
        return ResponseEntity.ok("Saque Otimista realizado com sucesso.");
    }
    
    @PostMapping("/{numero}/depositaPessimista")
    public ResponseEntity<String> depositaPessimista(@PathVariable String numero, 
                                                     @RequestBody ContaRequest contaRequest) {
        contaPessimistaService.deposita(numero, contaRequest.getValor());
        return ResponseEntity.ok("Depósito Pessimista realizado com sucesso.");
    }
    
    @PostMapping("/{numero}/saquePessimista")
    public ResponseEntity<String> saquePessimista(@PathVariable String numero, 
                                                  @RequestBody ContaRequest contaRequest) {
        contaPessimistaService.saque(numero, contaRequest.getValor());
        return ResponseEntity.ok("Saque Pessimista realizado com sucesso.");
    }
}