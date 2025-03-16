package com.guisebastiao.apispringboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guisebastiao.apispringboot.controller.dto.LoginDto;
import com.guisebastiao.apispringboot.controller.dto.SigninDto;
import com.guisebastiao.apispringboot.controller.dto.ResponseDto;
import com.guisebastiao.apispringboot.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService service;

  @PostMapping("/login")
  public ResponseEntity<ResponseDto> login(HttpServletResponse response, @RequestBody @Valid LoginDto dto) {
    this.service.login(response, dto);

    ResponseDto responseDto = new ResponseDto();

    responseDto.setStatus(HttpStatus.OK.value());
    responseDto.setValue(HttpStatus.OK.name());
    responseDto.setMessage("Bem vindo");

    return ResponseEntity.ok().body(responseDto);
  }

  @PostMapping("/signin")
  public ResponseEntity<ResponseDto> signin(HttpServletResponse response, @RequestBody @Valid SigninDto dto) {
    this.service.signin(response, dto);

    ResponseDto responseDto = new ResponseDto();

    responseDto.setStatus(HttpStatus.OK.value());
    responseDto.setValue(HttpStatus.OK.name());
    responseDto.setMessage("Cadastro finalizado com sucesso");

    return ResponseEntity.ok().body(responseDto);
  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseDto> logout(HttpServletResponse response) {
    this.service.logout(response);

    ResponseDto responseDto = new ResponseDto();

    responseDto.setStatus(HttpStatus.OK.value());
    responseDto.setValue(HttpStatus.OK.name());
    responseDto.setMessage("Volte sempre");

    return ResponseEntity.ok().body(responseDto);
  }
}
