package br.edu.iftm.tspi.sd.websockets.handler;


import br.edu.iftm.tspi.sd.websockets.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
public class WebSocketEventListener {


private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
private final ChatService chatService;


@EventListener
public void handleWebSocketConnectListener(SessionConnectEvent event) {
StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
String sessionId = accessor.getSessionId();
logger.info("Nova conexão WebSocket. sessionId={}", sessionId);
// O registro do usuário acontece via /app/chat.register (ChatController).
}


@EventListener
public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
String sessionId = accessor.getSessionId();
logger.info("Desconexão WebSocket detectada. sessionId={}", sessionId);
// Remove usuário pela sessão e atualiza lista
chatService.removerUsuarioPorSession(sessionId);
}
}