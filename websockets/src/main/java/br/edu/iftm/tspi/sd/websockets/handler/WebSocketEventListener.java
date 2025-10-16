package br.edu.iftm.tspi.sd.websockets.handler;

import br.edu.iftm.tspi.sd.websockets.service.ChatService;
import br.edu.iftm.tspi.sd.websockets.service.UsuarioSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UsuarioSessionService usuarioSessionService;
    private final ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Nova conexão WebSocket estabelecida: {}", headerAccessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        usuarioSessionService.getUsernamePorSession(sessionId).ifPresent(username -> {
            log.info("Usuário desconectado: {} (Session: {})", username, sessionId);
            usuarioSessionService.removerUsuarioPorSession(sessionId);
            chatService.notificarSaidaUsuario(username);
            chatService.atualizarListaUsuarios();
        });
        
        log.info("Conexão WebSocket fechada: {}", sessionId);
    }
}