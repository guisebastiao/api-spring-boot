package com.guisebastiao.apispringboot.exceptions;

public class AuthCredentialIncorrectException extends RuntimeException {
  public AuthCredentialIncorrectException(String message) {
    super(message);
  }
}
