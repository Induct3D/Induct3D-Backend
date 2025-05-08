package com.upao.induct3d.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upao.induct3d.backend.domain.MessageDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        logger.error("token not found or invalid");

        MessageDTO dto = new MessageDTO(HttpStatus.UNAUTHORIZED, "token not found or invalid");
        res.setContentType("application/json");
        res.setStatus(dto.getStatus().value());
        res.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        res.getWriter().flush();
        res.getWriter().close();
    }
}
