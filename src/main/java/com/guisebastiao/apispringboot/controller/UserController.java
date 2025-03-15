package com.guisebastiao.apispringboot.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guisebastiao.apispringboot.controller.dto.UserDto;
import com.guisebastiao.apispringboot.controller.mappers.UserMapper;
import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.service.UserService;

import jakarta.validation.Valid;
import lombok.Data;

@Data
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService service;
  private final UserMapper mapper;

  @PostMapping
  public ResponseEntity<String> create(@RequestBody @Valid UserDto dto) {
    User user = mapper.toEntity(dto);
    service.create(user);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<UserDto> findById(@PathVariable("id") String id) {
    return service.findById(id).map(user -> {
      UserDto dto = mapper.toDto(user);
      return ResponseEntity.ok(dto);
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    List<User> result = service.search(name, offset, limit);

    List<UserDto> list = result.stream().map(mapper::toDto).collect(Collectors.toList());

    return ResponseEntity.ok(list);
  }

  @PutMapping("{id}")
  public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Valid UserDto dto) {
    Optional<User> optionalUser = service.findById(id);

    if (optionalUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    service.update(optionalUser.get());

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> userDelete(@PathVariable("id") String id) {
    Optional<User> optionalUser = service.findById(id);

    if (optionalUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    service.delete(optionalUser.get());

    return ResponseEntity.noContent().build();
  }
}
