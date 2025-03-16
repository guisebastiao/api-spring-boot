package com.guisebastiao.apispringboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guisebastiao.apispringboot.controller.dto.LoginDto;
import com.guisebastiao.apispringboot.controller.dto.SigninDto;
import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.exceptions.AuthCredentialIncorrectException;
import com.guisebastiao.apispringboot.exceptions.UserAlreadyExistsException;
import com.guisebastiao.apispringboot.exceptions.UserNotFoundException;
import com.guisebastiao.apispringboot.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@Data
@Service
public class AuthService {

  @Value("${api.security.token.duration}")
  private String duration;

  @Value("${api.security.cookie.secure}")
  private String secure;

  private final UserRepository repository;
  private final PasswordEncoder encoder;
  private final TokenService tokenService;

  public String login(HttpServletResponse response, LoginDto dto) {
    Optional<User> userExist = this.repository.findByEmail(dto.getEmail());

    if (userExist.isEmpty()) {
      throw new UserNotFoundException("Esse usuário não está cadastrado");
    }

    User user = userExist.get();

    if (encoder.matches(dto.getPassword(), user.getPassword())) {
      String token = this.tokenService.generateToken(user);
      this.addJwtCookie(response, token);
      return token;
    }

    throw new AuthCredentialIncorrectException("Sua senha está incorreta");
  }

  public String signin(HttpServletResponse response, SigninDto dto) {
    Optional<User> userExist = this.repository.findByEmail(dto.getEmail());

    if (userExist.isEmpty()) {
      User user = new User();

      user.setName(dto.getName());
      user.setEmail(dto.getEmail());
      user.setPassword(encoder.encode(dto.getPassword()));

      this.repository.save(user);

      String token = this.tokenService.generateToken(user);
      this.addJwtCookie(response, token);
      return token;
    }

    throw new UserAlreadyExistsException("Esse usuário já está cadastrado");
  }

  public void logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("Authenticate", null);
    cookie.setPath("/");
    cookie.setMaxAge(0);

    response.addCookie(cookie);
  }

  private void addJwtCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie("Authenticate", token);

    cookie.setPath("/");
    cookie.setMaxAge(Integer.parseInt(duration));
    cookie.setHttpOnly(false);
    cookie.setSecure(Boolean.parseBoolean(secure));

    response.addCookie(cookie);
  }
}
