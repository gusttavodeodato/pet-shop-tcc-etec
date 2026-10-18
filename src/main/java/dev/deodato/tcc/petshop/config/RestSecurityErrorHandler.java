package dev.deodato.tcc.petshop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.deodato.tcc.petshop.exception.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RestSecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestSecurityErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        escrever(response, HttpStatus.UNAUTHORIZED, "Não autenticado",
                "Token ausente, inválido ou expirado.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        escrever(response, HttpStatus.FORBIDDEN, "Acesso negado",
                "Você não tem permissão para acessar este recurso.");
    }

    private void escrever(HttpServletResponse response, HttpStatus status, String titulo, String mensagem) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErroResponse erro = new ErroResponse(LocalDateTime.now(), status.value(), titulo, List.of(mensagem));
        objectMapper.writeValue(response.getOutputStream(), erro);
    }
}
