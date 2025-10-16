package br.edu.iftm.tspi.sd.websockets.dto;

import br.edu.iftm.tspi.sd.websockets.model.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private String username;
    private StatusUsuario status;
    private LocalDateTime ultimaConexao;
    private String sessionId;
}