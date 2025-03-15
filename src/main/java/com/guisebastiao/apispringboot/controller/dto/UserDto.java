package com.guisebastiao.apispringboot.controller.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
  UUID id;

  @NotBlank(message = "Por favor, infome seu nome")
  @Size(min = 3, message = "Seu nome precisa ser maior que 3 caracteres")
  @Size(max = 50, message = "Seu nome precisa ser menor que 50 caracteres")
  String name;

  @NotBlank(message = "Por favor, infome seu email")
  @Email(message = "E-mail inválido")
  @Size(max = 250, message = "Seu email precisa ser menor que 250 caracteres")
  String email;

  @NotBlank(message = "Por favor, infome sua senha")
  @Size(min = 8, message = "A senha precisa ter no mínimo 8 caracteres")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d)(?=.*[!@#$]).{8,}$", message = "A senha deve conter pelo menos uma letra maiúscula, dois números e um dos seguintes caracteres especiais: !, @, #, $")
  String password;
}
