package com.guisebastiao.apispringboot.controller.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.guisebastiao.apispringboot.controller.dto.UserResponseDto;
import com.guisebastiao.apispringboot.controller.dto.UserRequestDto;
import com.guisebastiao.apispringboot.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "email", target = "email")
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)

  User toEntity(UserRequestDto dto);

  UserResponseDto toDto(User user);
}
