package com.guisebastiao.apispringboot.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.exceptions.UserAlreadyExistsException;
import com.guisebastiao.apispringboot.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {

  private final UserRepository repository;

  public void create(User user) {
    Optional<User> existUser = repository.findByEmail(user.getEmail());

    if (existUser.isPresent()) {
      throw new UserAlreadyExistsException("Esse usuário já está cadastrado");
    }

    repository.save(user);
  }

  public Optional<User> findById(String id) {
    var userId = UUID.fromString(id);
    return repository.findById(userId);
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

  public void update(User user) {
    if (user.getId() == null) {
      throw new IllegalArgumentException("Usuário não encontrado");
    }

    repository.save(user);
  }

  public void delete(User user) {
    repository.delete(user);
  }
}
