package br.edu.iftm.tspi.sd.websockets.service;

import br.edu.iftm.tspi.sd.websockets.dto.UsuarioDto;
import br.edu.iftm.tspi.sd.websockets.model.StatusUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UsuarioSessionService {

    private final Map<String, UsuarioDto> usuariosConectados = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUsuarioMap = new ConcurrentHashMap<>();

    public boolean registrarUsuario(UsuarioDto usuario, String sessionId) {
        String username = normalizar(usuario.getUsername());
        if (username == null || username.isBlank()) return false;

        usuario.setUsername(username);

        if (usuariosConectados.putIfAbsent(username, usuario) == null) {
            usuario.setStatus(StatusUsuario.ONLINE);
            // UsuarioDto does not define setDataConexao(LocalDateTime); remove the call here
            // If you need to track connection time, either add a field + setter to UsuarioDto
            // or store the timestamp in a separate structure within this service.
            sessionUsuarioMap.put(sessionId, username);
            log.info("Usuário '{}' registrado na sessão {}.", username, sessionId);
            return true;
        }
        return false;
    }

    public Optional<UsuarioDto> removerUsuarioPorSession(String sessionId) {
        String username = sessionUsuarioMap.remove(sessionId);
        if (username != null) {
            UsuarioDto removed = usuariosConectados.remove(username);
            log.info("Usuário '{}' desconectado (sessionId={})", username, sessionId);
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
