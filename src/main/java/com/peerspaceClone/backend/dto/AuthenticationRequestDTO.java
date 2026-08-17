package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
    @NotBlank(message = "Username/Email is required")
    String username,

    @NotBlank(message = "Password is required")
    String password
) {
}
