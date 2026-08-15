package com.peerspaceClone.backend.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public record BlockedSlotInsertDTO(
    @NotNull(message = "Property ID is required")
    Long propertyId,

    @NotNull(message = "Start time is required")
    LocalDateTime startTime,

    @NotNull(message = "End time is required")
    LocalDateTime endTime,

    String reason
) {
}
