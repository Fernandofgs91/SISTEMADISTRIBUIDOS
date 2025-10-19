package br.edu.iftm.tspi.sd.websockets.service;

import br.edu.iftm.tspi.sd.websockets.dto.UsuarioDto;
import br.edu.iftm.tspi.sd.websockets.model.StatusUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UsuarioSessionService {

    private final Map<String, UsuarioDto> usuariosConectados = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUsuarioMap = new ConcurrentHashMap<>();

    /**
     * Registra um usuário associado à sessionId.
     * Normaliza o username (trim + lowercase) para evitar duplicidade trivial.
     */
    public boolean registrarUsuario(UsuarioDto usuario, String sessionId) {
        String username = normalizar(usuario.getUsername());
        if (username == null || username.isBlank()) return false;

        usuario.setUsername(username);

        if (usuariosConectados.putIfAbsent(username, usuario) == null) {
            usuario.setStatus(StatusUsuario.ONLINE);
            // connection timestamp removed here because UsuarioDto does not define setDataConexao(LocalDateTime)
            sessionUsuarioMap.put(sessionId, username);
            log.info("Usuario '{}' registrado na sessão {}", username, sessionId);
            return true;
        }
        return false;
    }

    /**
     * Remove usuário baseado na sessionId (ex: desconexão)
     */
    public Optional<UsuarioDto> removerUsuarioPorSession(String sessionId) {
        String username = sessionUsuarioMap.remove(sessionId);
        if (username != null) {
            UsuarioDto removed = usuariosConectados.remove(username);
            log.info("Usuario '{}' removido (sessionId={})", username, sessionId);
            return Optional.ofNullable(removed);
        }
        return Optional.empty();
    }

    public List<String> listarUsuariosOnline() {
        return new ArrayList<>(usuariosConectados.keySet());
    }

    public boolean existeUsuario(String username) {
        return usuariosConectados.containsKey(normalizar(username));
    }

    public Optional<UsuarioDto> getUsuario(String username) {
        return Optional.ofNullable(usuariosConectados.get(normalizar(username)));
    }

    public Optional<String> getUsernamePorSession(String sessionId) {
        return Optional.ofNullable(sessionUsuarioMap.get(sessionId));
    }

    public int getQuantidadeUsuariosOnline() {
        return usuariosConectados.size();
    }

    private String normalizar(String nome) {
        return nome == null ? null : nome.trim().toLowerCase(Locale.ROOT);
    }
}
