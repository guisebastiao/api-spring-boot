package com.guisebastiao.apispringboot.controller.dto;

import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email) {
}
