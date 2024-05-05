package com.clipstory.clipstoryserver.global.auth;

import com.clipstory.clipstoryserver.global.response.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            if (Status.JWT_WRONG_TYPE_TOKEN.getMessage().equals(message)) {
                setResponse(response, Status.JWT_WRONG_TYPE_TOKEN);
            }
            else if (Status.JWT_EXPIRED_TOKEN.getMessage().equals(message)) {
                setResponse(response, Status.JWT_EXPIRED_TOKEN);
            }
            else if (Status.JWT_INVALID.getMessage().equals(message)) {
                setResponse(response, Status.JWT_INVALID);
            }
        }
    }

    @JsonPropertyOrder({"success", "status", "message", "data"})
    private void setResponse(HttpServletResponse response, Status status) throws RuntimeException, IOException {
        final Map<String, Object> body = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        body.put("success", false);
        body.put("status", status.getHttpStatus());
        body.put("code", status.getCode());
        body.put("message", status.getMessage());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

}

