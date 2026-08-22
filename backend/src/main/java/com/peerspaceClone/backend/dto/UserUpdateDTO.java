package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    @NotNull
    @Size(min = 3, max = 17, message = "Username must be between 3 and 17 characters")
    String username,

    @NotNull
    @Email(message = "Email is not valid")
    String email,

    @NotNull
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,

    @NotNull
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName
) {
}
