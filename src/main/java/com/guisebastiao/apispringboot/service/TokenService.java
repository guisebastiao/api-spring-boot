package com.guisebastiao.apispringboot.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.guisebastiao.apispringboot.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  @Value("${api.security.token.duration}")
  private String duration;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create()
          .withIssuer("login-auth-api")
          .withSubject(user.getId().toString())
          .withExpiresAt(this.generateExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error while authenticating");
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer("login-auth-api")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  private Instant generateExpirationDate() {
    int jwtDuration = Integer.parseInt(duration);
    return LocalDateTime.now().plusSeconds(jwtDuration).toInstant(ZoneOffset.of("-03:00"));
  }
}
