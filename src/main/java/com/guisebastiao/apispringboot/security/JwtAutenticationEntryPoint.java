package com.guisebastiao.apispringboot.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guisebastiao.apispringboot.controller.dto.ResponseDto;

import java.io.IOException;

@Component
public class JwtAutenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    ResponseDto error = new ResponseDto();

    error.setStatus(HttpStatus.UNAUTHORIZED.value());
    error.setValue(HttpStatus.UNAUTHORIZED.name());
    error.setMessage("Por favor, para continuar é necessario o seu login");

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    response.getWriter().write(this.convertToJson(error));
  }

  private String convertToJson(ResponseDto error) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(error);
    } catch (Exception e) {
      throw new RuntimeException("Algo deu errado, tente novamente mais tarde");
    }
  }
}
