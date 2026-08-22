package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookingCancelDTO(
    @NotBlank(message = "Cancelled-by actor is required")
    String canceledBy,

    @NotBlank(message = "Cancellation reason is required")
    @Size(min = 5, max = 255, message = "Reason must be between 5 and 255 characters")
    String cancelationReason
) {
}
