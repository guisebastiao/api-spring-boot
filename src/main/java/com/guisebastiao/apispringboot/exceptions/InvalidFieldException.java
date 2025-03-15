package com.guisebastiao.apispringboot.exceptions;

public class InvalidFieldException extends RuntimeException {
  private String field;

  public InvalidFieldException(String field, String message) {
    super(message);
    this.field = field;
  }

  public String getField() {
    return field;
  }

  public void setField(String campo) {
    this.field = campo;
  }
}
