package br.edu.iftm.tspi.sd.websockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // <-- Adicionar importação
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity // <-- Adicionar anotação
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita a proteção CSRF, comum em APIs e WebSockets
            .csrf(csrf -> csrf.disable()) 
            // Configura as regras de autorização para as requisições HTTP
            .authorizeHttpRequests(authz -> authz
                // Permite todas as requisições sem autenticação
                .anyRequest().permitAll() 
            );
        return http.build();
    }
}