package com.guisebastiao.apispringboot.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.repository.UserRepository;
import com.guisebastiao.apispringboot.service.TokenService;

import jakarta.servlet.FilterChain;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Autowired
  TokenService tokenService;
  @Autowired
  UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    var token = this.recoverToken(request, response);
    var login = tokenService.validateToken(token);

    if (login != null) {
      UUID userId = UUID.fromString(login);

      Optional<User> user = userRepository.findById(userId);

      var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
      var authentication = new UsernamePasswordAuthenticationToken(user, true, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      request.setAttribute("userId", userId);
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    String token = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("Authenticate".equals(cookie.getName())) {
          token = cookie.getValue();
        }
      }
    }

    if (token == null) {
      Cookie cookie = new Cookie("Authenticate", "");
      cookie.setMaxAge(0);
      cookie.setPath("/");
      response.addCookie(cookie);
      return null;
    }

    return token;
  }
}
