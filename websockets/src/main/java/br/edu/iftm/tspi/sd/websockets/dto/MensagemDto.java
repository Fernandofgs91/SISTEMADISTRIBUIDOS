package br.edu.iftm.tspi.sd.websockets.dto;

import br.edu.iftm.tspi.sd.websockets.model.TipoMensagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemDto {
    private TipoMensagem tipoMensagem;
    private String remetente; // Alterado de 'origem'
    private String destino;
    private String conteudo;  // Alterado de 'texto'
    private LocalDateTime dataHora;
    private String sessionId;
}