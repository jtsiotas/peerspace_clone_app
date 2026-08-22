package com.peerspaceClone.backend.dto;

import java.time.LocalDateTime;

public record BlockedSlotReadOnlyDTO(
    Long id,
    Long propertyId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String reason
) {
}
