package br.edu.iftm.tspi.sd.websockets.service;

import br.edu.iftm.tspi.sd.websockets.dto.UsuarioDto;
import br.edu.iftm.tspi.sd.websockets.model.StatusUsuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UsuarioSessionService {

    private final Map<String, UsuarioDto> usuariosConectados = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUsuarioMap = new ConcurrentHashMap<>();

    public void adicionarUsuario(String username, String sessionId) {
        UsuarioDto usuario = new UsuarioDto(
            username, 
            StatusUsuario.ONLINE, 
            LocalDateTime.now(), 
            sessionId
        );
        usuariosConectados.put(username, usuario);
        sessionUsuarioMap.put(sessionId, username);
    }

    public void removerUsuario(String username) {
        UsuarioDto usuario = usuariosConectados.remove(username);
        if (usuario != null) {
            sessionUsuarioMap.remove(usuario.getSessionId());
        }
    }

    public void removerUsuarioPorSession(String sessionId) {
        String username = sessionUsuarioMap.remove(sessionId);
        if (username != null) {
            usuariosConectados.remove(username);
        }
    }

    public Set<String> listarUsuariosOnline() {
        return Collections.unmodifiableSet(usuariosConectados.keySet());
    }

    public boolean usuarioExiste(String username) {
        return usuariosConectados.containsKey(username);
    }

    public Optional<UsuarioDto> getUsuario(String username) {
        return Optional.ofNullable(usuariosConectados.get(username));
    }

    public Optional<String> getUsernamePorSession(String sessionId) {
        return Optional.ofNullable(sessionUsuarioMap.get(sessionId));
    }

    public int getQuantidadeUsuariosOnline() {
        return usuariosConectados.size();
    }
}