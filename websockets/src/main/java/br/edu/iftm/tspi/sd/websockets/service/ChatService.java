package br.edu.iftm.tspi.sd.websockets.service;

import br.edu.iftm.tspi.sd.websockets.dto.MensagemDto;
import br.edu.iftm.tspi.sd.websockets.dto.RespostaChatDto;
import br.edu.iftm.tspi.sd.websockets.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioSessionService usuarioSessionService;

    public void registrarUsuario(UsuarioDto usuario, String sessionId) {
        boolean ok = usuarioSessionService.registrarUsuario(usuario, sessionId);
        if (ok) {
            atualizarListaUsuarios();
            log.info("Usuário '{}' registrado. Online: {}", usuario.getUsername(), usuarioSessionService.getQuantidadeUsuariosOnline());
        } else {
            throw new IllegalStateException("Usuário já conectado: " + usuario.getUsername());
        }
    }

    public void removerUsuarioPorSession(String sessionId) {
        usuarioSessionService.removerUsuarioPorSession(sessionId);
        atualizarListaUsuarios();
        log.info("Removido usuário da sessão {}", sessionId);
    }

    public void enviarMensagemPublica(MensagemDto mensagem) {
        RespostaChatDto resposta = new RespostaChatDto(
                "PUBLICA",
                mensagem,
                LocalDateTime.now(),
                List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );
        messagingTemplate.convertAndSend("/topic/public", resposta);
        log.info("Mensagem pública de '{}'", mensagem.getRemetente());
    }

    public void enviarMensagemPrivada(MensagemDto mensagem) {
        String destino = mensagem.getDestino();
        RespostaChatDto resposta = new RespostaChatDto(
                "PRIVADA",
                mensagem,
                LocalDateTime.now(),
                List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );

        // Envia ao usuário destino na fila /user/{destino}/queue/private
        messagingTemplate.convertAndSendToUser(destino, "/queue/private", resposta);

        // Envia também ao remetente para confirmação (opcional)
        messagingTemplate.convertAndSendToUser(mensagem.getRemetente(), "/queue/private", resposta);

        log.info("Mensagem privada de '{}' para '{}'", mensagem.getRemetente(), destino);
    }

    public void atualizarListaUsuarios() {
        RespostaChatDto resposta = new RespostaChatDto(
                "ATUALIZAR_USUARIOS",
                null,
                LocalDateTime.now(),
                List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );
        messagingTemplate.convertAndSend("/topic/usuarios", resposta);
    }
}
