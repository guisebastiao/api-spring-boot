package com.guisebastiao.apispringboot.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guisebastiao.apispringboot.controller.dto.ResponseDto;
import com.guisebastiao.apispringboot.controller.dto.UserRequestDto;
import com.guisebastiao.apispringboot.controller.dto.UserResponseDto;
import com.guisebastiao.apispringboot.controller.mappers.UserMapper;
import com.guisebastiao.apispringboot.entity.User;
import com.guisebastiao.apispringboot.exceptions.UserNotFoundException;
import com.guisebastiao.apispringboot.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService service;
  private final UserMapper mapper;

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable("id") String id) {
    Optional<User> user = service.findById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException("Usuário não foi encontrado");
    }

    UserResponseDto dto = mapper.toDto(user.get());

    return ResponseEntity.ok().body(dto);
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    List<User> result = service.search(name, offset, limit);

    List<UserResponseDto> list = result.stream().map(mapper::toDto).collect(Collectors.toList());

    return ResponseEntity.ok(list);
  }

  @PutMapping
  public ResponseEntity<ResponseDto> update(@RequestBody @Valid UserRequestDto dto, HttpServletRequest request) {
    Object userId = request.getAttribute("userId");

    service.update(dto, userId);

    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.OK.value());
    response.setValue(HttpStatus.OK.name());
    response.setMessage("Seu perfi foi atualizado com sucesso");

    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping
  public ResponseEntity<ResponseDto> userDelete(HttpServletRequest request) {
    Object userId = request.getAttribute("userId");

    service.delete(userId);

    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.OK.value());
    response.setValue(HttpStatus.OK.name());
    response.setMessage("Sua conta foi deletada com sucesso");

    return ResponseEntity.ok().body(response);
  }
}
