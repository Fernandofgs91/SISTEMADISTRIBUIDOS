package br.edu.iftm.tspi.sd.websockets.service;

import br.edu.iftm.tspi.sd.websockets.dto.MensagemDto;
import br.edu.iftm.tspi.sd.websockets.dto.RespostaChatDto;
import br.edu.iftm.tspi.sd.websockets.model.TipoMensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioSessionService usuarioSessionService;

    public void enviarMensagemPublica(MensagemDto mensagem) {
        RespostaChatDto resposta = new RespostaChatDto(
            "MENSAGEM_PUBLICA",
            mensagem,
            LocalDateTime.now(),
            List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );
        
        messagingTemplate.convertAndSend("/topic/public", resposta);
    }

    public void enviarMensagemPrivada(MensagemDto mensagem) {
        RespostaChatDto resposta = new RespostaChatDto(
            "MENSAGEM_PRIVADA",
            mensagem,
            LocalDateTime.now(),
            null
        );
        
        messagingTemplate.convertAndSendToUser(
            mensagem.getDestino(), 
            "/queue/private", 
            resposta
        );
    }

    public void notificarEntradaUsuario(String username) {
        MensagemDto mensagem = new MensagemDto(
            TipoMensagem.ENTRAR,
            username,
            null,
            username + " entrou no chat",
            LocalDateTime.now(),
            null
        );
        
        RespostaChatDto resposta = new RespostaChatDto(
            "USUARIO_ENTROU",
            mensagem,
            LocalDateTime.now(),
            List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );
        
        messagingTemplate.convertAndSend("/topic/public", resposta);
    }

    public void notificarSaidaUsuario(String username) {
        MensagemDto mensagem = new MensagemDto(
            TipoMensagem.SAIR,
            username,
            null,
            username + " saiu do chat",
            LocalDateTime.now(),
            null
        );
        
        RespostaChatDto resposta = new RespostaChatDto(
            "USUARIO_SAIU",
            mensagem,
            LocalDateTime.now(),
            List.copyOf(usuarioSessionService.listarUsuariosOnline())
        );
        
        messagingTemplate.convertAndSend("/topic/public", resposta);
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