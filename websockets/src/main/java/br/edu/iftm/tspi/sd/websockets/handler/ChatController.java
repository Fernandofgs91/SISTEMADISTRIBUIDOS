package br.edu.iftm.tspi.sd.websockets.handler;

import br.edu.iftm.tspi.sd.websockets.dto.MensagemDto;
import br.edu.iftm.tspi.sd.websockets.exception.UsuarioExistenteException;
import br.edu.iftm.tspi.sd.websockets.model.TipoMensagem;
import br.edu.iftm.tspi.sd.websockets.service.ChatService;
import br.edu.iftm.tspi.sd.websockets.service.UsuarioSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UsuarioSessionService usuarioSessionService;

    @MessageMapping("/chat.entrar")
    @SendToUser("/queue/join")
    public String entrar(@Payload MensagemDto mensagem, SimpMessageHeaderAccessor headerAccessor) {
        String username = mensagem.getOrigem();
        String sessionId = headerAccessor.getSessionId();
        
        if (usuarioSessionService.usuarioExiste(username)) {
            // A exceção agora é lançada corretamente.
            throw new UsuarioExistenteException("Usuário " + username + " já está online!", null, null);
        }
        
        // **MELHORIA:** Guarda o nome do usuário na sessão para fácil acesso posterior.
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", username);
        
        usuarioSessionService.adicionarUsuario(username, sessionId);
        chatService.notificarEntradaUsuario(username);
        chatService.atualizarListaUsuarios();
        
        return "SUCCESS";
    }

    @MessageMapping("/chat.enviar")
    public void enviarMensagem(@Payload MensagemDto mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.ENVIAR_TEXTO);
        mensagem.setDataHora(LocalDateTime.now());
        
        chatService.enviarMensagemPublica(mensagem);
    }

    @MessageMapping("/chat.privado")
    public void enviarMensagemPrivada(@Payload MensagemDto mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.PRIVADO);
        mensagem.setDataHora(LocalDateTime.now());
        
        chatService.enviarMensagemPrivada(mensagem);
    }

    @SuppressWarnings("null")
    @MessageMapping("/chat.sair")
    public void sair(@Payload MensagemDto mensagem, SimpMessageHeaderAccessor headerAccessor) {
        // **MELHORIA (Opcional):** Recupera o nome da sessão para mais segurança.
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null && username.equals(mensagem.getOrigem())) {
            usuarioSessionService.removerUsuario(username);
            chatService.notificarSaidaUsuario(username);
            chatService.atualizarListaUsuarios();
        }
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload MensagemDto mensagem) {
        // **MELHORIA:** Lógica refatorada para um método privado.
        handleTypingEvent(mensagem, TipoMensagem.TYPING);
    }

    @MessageMapping("/chat.stopTyping")
    public void stopTyping(@Payload MensagemDto mensagem) {
        // **MELHORIA:** Lógica refatorada para um método privado.
        handleTypingEvent(mensagem, TipoMensagem.STOP_TYPING);
    }

    /**
     * **NOVO MÉTODO (Refatoração):** Encapsula a lógica de eventos de digitação.
     */
    private void handleTypingEvent(MensagemDto mensagem, TipoMensagem tipo) {
        mensagem.setTipoMensagem(tipo);
        mensagem.setDataHora(LocalDateTime.now());
        
        boolean isPrivate = mensagem.getDestino() != null && !mensagem.getDestino().isBlank();
        
        if (isPrivate) {
            // Typing em chat privado
            chatService.enviarMensagemPrivada(mensagem);
        } else {
            // Typing em chat público
            chatService.enviarMensagemPublica(mensagem);
        }
    }
}