package com.guisebastiao.apispringboot.controller.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.guisebastiao.apispringboot.controller.dto.FieldErrorDto;
import com.guisebastiao.apispringboot.controller.dto.ResponseDto;
import com.guisebastiao.apispringboot.exceptions.AuthCredentialIncorrectException;
import com.guisebastiao.apispringboot.exceptions.UserAlreadyExistsException;
import com.guisebastiao.apispringboot.exceptions.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getFieldErrors();

    List<FieldErrorDto> errors = fieldErrors.stream().map(error -> {
      return new FieldErrorDto(error.getField(), error.getDefaultMessage());
    }).collect(Collectors.toList());

    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    response.setValue(HttpStatus.UNPROCESSABLE_ENTITY.name());
    response.setMessage("Erro de validação");
    response.setDetails(errors);

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.CONFLICT.value());
    response.setValue(HttpStatus.CONFLICT.name());
    response.setMessage(e.getMessage());
    response.setDetails(null);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ResponseDto> handleUserNotFound(UserNotFoundException e) {
    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.NOT_FOUND.value());
    response.setValue(HttpStatus.NOT_FOUND.name());
    response.setMessage(e.getMessage());
    response.setDetails(null);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(AuthCredentialIncorrectException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ResponseDto> handleAuthCredentialIncorrectException(AuthCredentialIncorrectException e) {
    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setValue(HttpStatus.UNAUTHORIZED.name());
    response.setMessage(e.getMessage());
    response.setDetails(null);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ResponseDto> handleGenericException(InternalServerError e) {
    ResponseDto response = new ResponseDto();

    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setValue(HttpStatus.INTERNAL_SERVER_ERROR.name());
    response.setMessage(e.getMessage());
    response.setDetails(null);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
