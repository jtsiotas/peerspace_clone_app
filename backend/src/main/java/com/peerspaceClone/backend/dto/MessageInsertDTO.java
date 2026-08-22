package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageInsertDTO(
    @NotNull(message = "Booking ID is required")
    Long bookingId,

    @NotNull(message = "Sender ID is required")
    Long senderId,

    @NotBlank(message = "Message content cannot be blank")
    String content
) {
}
