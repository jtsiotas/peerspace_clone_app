package com.peerspaceClone.backend.dto;

public record ErrorResponseDTO(
    String code,
    String description
) {
}
