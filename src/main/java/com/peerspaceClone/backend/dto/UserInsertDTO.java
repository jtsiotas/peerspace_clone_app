package com.peerspaceClone.backend.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(
    @NotNull
    @Size(min = 3, max = 17, message = "Username must be between 3 and 17 characters")  
    String username,
    @NotNull
    @Size(min = 8, max = 24, message = "Password must be between 8 and 24 characters")
    String password,
    @NotNull
    @Email(message = "Email is not valid")
    String email,
    @NotNull
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,
    @NotNull
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,
    @NotEmpty(message = "Roles cannot be empty")
    Set<Long> roleIds
) {
    
}
    