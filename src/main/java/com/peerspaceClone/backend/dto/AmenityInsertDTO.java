package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AmenityInsertDTO(
    @NotBlank(message = "Amenity name is required")
    String name,

    String iconUrl
) {
}
