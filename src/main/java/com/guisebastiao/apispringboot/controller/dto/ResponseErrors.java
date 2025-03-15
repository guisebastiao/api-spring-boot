package com.guisebastiao.apispringboot.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseErrors(int status, String message, List<FieldErrors> error) {

  public static ResponseErrors defaultResponse(String message) {
    return new ResponseErrors(HttpStatus.BAD_REQUEST.value(), message, List.of());
  }

  public static ResponseErrors conflict(String message) {
    return new ResponseErrors(HttpStatus.CONFLICT.value(), message, List.of());
  }

  public static ResponseErrors notFound(String message) {
    return new ResponseErrors(HttpStatus.NOT_FOUND.value(), message, List.of());
  }

  public static ResponseErrors internalError(String message) {
    return new ResponseErrors(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, List.of());
  }
}
