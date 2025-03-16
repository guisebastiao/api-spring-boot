package com.guisebastiao.apispringboot.controller.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResponseDto {
  private int status;
  private String value;
  private String message;
  private List<FieldErrorDto> details;
}
