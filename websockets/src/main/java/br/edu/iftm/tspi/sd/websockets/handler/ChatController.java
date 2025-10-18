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

    @MessageMapping("/chat.register")
    public void register(@Payload UsuarioDto usuario, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        try {
            chatService.registrarUsuario(usuario, sessionId);
            log.info("Usuário '{}' registrado com sucesso.", usuario.getUsername());
        } catch (IllegalStateException e) {
            // --- CORRIGIDO ---
            // Lança a exceção passando a mensagem de erro real (e.getMessage())
            // e o objeto 'usuario' como dados, em vez do 'sessionId' e da exceção 'e'.
            log.warn("Erro ao registrar usuário: {}", e.getMessage());
            throw new UsuarioExistenteException(e.getMessage(), usuario, null);
            // --- FIM DA CORREÇÃO ---
        }
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MensagemDto mensagem) {
        mensagem.setDataHora(LocalDateTime.now());
        // Esta verificação está correta, pois identifica se há um destinatário específico
        boolean isPrivate = mensagem.getDestino() != null && !mensagem.getDestino().isBlank();

        if (isPrivate) {
            chatService.enviarMensagemPrivada(mensagem);
        } else {
            chatService.enviarMensagemPublica(mensagem);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleUserExists(UsuarioExistenteException exception) {
        log.warn("Erro ao registrar usuário: {}", exception.getMessage());
        return exception.getMessage();
    }
}