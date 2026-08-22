package com.peerspaceClone.backend.dto;

import java.time.Instant;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record BookingInsertDTO(
    @NotNull(message = "Property ID is required")
    Long propertyId,

    @NotNull(message = "Guest ID is required")
    Long guestId,

    @NotNull(message = "Start date and time are required")
    @Future(message = "Start date must be in the future")
    Instant startDatetime,

    @NotNull(message = "End date and time are required")
    @Future(message = "End date must be in the future")
    Instant endDatetime
) {
}
