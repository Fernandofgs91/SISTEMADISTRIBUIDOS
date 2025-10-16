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
            throw new UsuarioExistenteException("Usuário " + username + " já está online!", sessionId, null, null);
        }
        
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

    @MessageMapping("/chat.sair")
    public void sair(@Payload MensagemDto mensagem) {
        String username = mensagem.getOrigem();
        usuarioSessionService.removerUsuario(username);
        chatService.notificarSaidaUsuario(username);
        chatService.atualizarListaUsuarios();
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload MensagemDto mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.TYPING);
        mensagem.setDataHora(LocalDateTime.now());
        
        if (mensagem.getDestino() != null) {
            // Typing em chat privado
            chatService.enviarMensagemPrivada(mensagem);
        } else {
            // Typing em chat público
            chatService.enviarMensagemPublica(mensagem);
        }
    }

    @MessageMapping("/chat.stopTyping")
    public void stopTyping(@Payload MensagemDto mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.STOP_TYPING);
        mensagem.setDataHora(LocalDateTime.now());
        
        if (mensagem.getDestino() != null) {
            chatService.enviarMensagemPrivada(mensagem);
        } else {
            chatService.enviarMensagemPublica(mensagem);
        }
    }
}