package com.guisebastiao.apispringboot.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guisebastiao.apispringboot.controller.dto.UserRequestDto;
import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.exceptions.UserNotFoundException;
import com.guisebastiao.apispringboot.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {

  private final UserRepository repository;

  private final PasswordEncoder encoder;

  public Optional<User> findById(String id) {
    var userId = UUID.fromString(id);
    Optional<User> user = repository.findById(userId);
    return user;
  }

  public List<User> search(String name, int offset, int limit) {
    User user = new User();

    user.setName(name);

    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnorePaths("id", "email", "password")
        .withIgnoreNullValues()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    Example<User> userExample = Example.of(user, matcher);

    Pageable pageable = PageRequest.of(offset, limit);

    Page<User> resultPage = repository.findAll(userExample, pageable);

    return resultPage.getContent();
  }

  public void update(UserRequestDto dto, Object userId) {
    UUID id = UUID.fromString(userId.toString());

    Optional<User> userFound = repository.findById(id);

    if (userFound.isEmpty()) {
      throw new UserNotFoundException("Usuário não foi encontrado");
    }

    User user = new User();

    user.setId(id);
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPassword(encoder.encode(dto.getPassword()));
    user.setCreatedAt(userFound.get().getCreatedAt());

    repository.save(user);
  }

  public void delete(Object userId) {
    UUID id = UUID.fromString(userId.toString());

    Optional<User> user = repository.findById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException("Usuário não foi encontrado");
    }

    repository.delete(user.get());
  }
}
