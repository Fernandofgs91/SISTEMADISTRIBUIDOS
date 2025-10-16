package br.edu.iftm.tspi.sd.websockets.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioExistenteException {
    private String tipo;
    private Object dados;
    private LocalDateTime timestamp;
    private List<String> usuariosOnline;
}