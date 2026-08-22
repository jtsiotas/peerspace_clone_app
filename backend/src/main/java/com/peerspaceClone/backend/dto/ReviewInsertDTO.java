package com.peerspaceClone.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewInsertDTO(
    @NotNull(message = "Booking ID is required")
    Long bookingId,

    @NotNull(message = "Reviewer ID is required")
    Long reviewerId,

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    int rating,

    String comment,

    Boolean isPublic
) {
}
