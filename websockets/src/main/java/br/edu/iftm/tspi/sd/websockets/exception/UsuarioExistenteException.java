package br.edu.iftm.tspi.sd.websockets.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UsuarioExistenteException extends RuntimeException {
    // Mantive os campos para que você possa usá-los em um @MessageExceptionHandler
    private final Object dados;
    private final LocalDateTime timestamp;
    private final List<String> usuariosOnline;

    public UsuarioExistenteException(String message, Object dados, List<String> usuariosOnline) {
        super(message); // Passa a mensagem de erro para a classe pai
        this.dados = dados;
        this.usuariosOnline = usuariosOnline;
        this.timestamp = LocalDateTime.now();
    }
}