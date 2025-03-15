package com.guisebastiao.apispringboot.controller.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.guisebastiao.apispringboot.controller.dto.FieldErrors;
import com.guisebastiao.apispringboot.controller.dto.ResponseErrors;
import com.guisebastiao.apispringboot.exceptions.InvalidFieldException;
import com.guisebastiao.apispringboot.exceptions.UserAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getFieldErrors();
    List<FieldErrors> errorsList = fieldErrors.stream()
        .map(fe -> new FieldErrors(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());

    return new ResponseErrors(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", errorsList);
  }

  @ExceptionHandler(InvalidFieldException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseErrors handleCampoInvalidoException(InvalidFieldException e) {
    return new ResponseErrors(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "Erro de validação",
        List.of(new FieldErrors(e.getField(), e.getMessage())));
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseErrors handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    return ResponseErrors.conflict(e.getMessage());
  }

  @ExceptionHandler(JWTCreationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseErrors handleJWTCreationException(JWTCreationException e) {
    return ResponseErrors.internalError(e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseErrors handleGenericException() {
    return new ResponseErrors(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Algo deu errado, tente novamente mais tarde",
        List.of());
  }
}
