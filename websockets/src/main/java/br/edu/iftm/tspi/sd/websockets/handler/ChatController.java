package br.edu.iftm.tspi.sd.websockets.handler;

import br.edu.iftm.tspi.sd.websockets.dto.MensagemDto;
import br.edu.iftm.tspi.sd.websockets.dto.UsuarioDto;
import br.edu.iftm.tspi.sd.websockets.exception.UsuarioExistenteException;
import br.edu.iftm.tspi.sd.websockets.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Registro do usuário. Espera um payload JSON com pelo menos { "username": "nome" }.
     * O cliente deve enviar para /app/chat.register após conectar.
     */
    @MessageMapping("/chat.register")
    public void register(@Payload UsuarioDto usuario, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        try {
            chatService.registrarUsuario(usuario, sessionId);
            log.info("Usuário '{}' registrado (sessionId={})", usuario.getUsername(), sessionId);
        } catch (IllegalStateException e) {
            // Repropaga IllegalStateException para ser tratada por handleUserExists()
            throw e;
        }
    }

    /**
     * Envio de mensagem pública ou privada.
     * Mensagem privada contém o campo destino (username).
     * O cliente envia para /app/chat.send com um objeto MensagemDto.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MensagemDto mensagem) {
        mensagem.setDataHora(LocalDateTime.now());
        boolean isPrivate = mensagem.getDestino() != null && !mensagem.getDestino().isBlank();

        if (isPrivate) {
            chatService.enviarMensagemPrivada(mensagem);
        } else {
            chatService.enviarMensagemPublica(mensagem);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleUserExists(IllegalStateException exception) {
        log.warn("Erro no registro de usuário: {}", exception.getMessage());
        return exception.getMessage();
    }
}
