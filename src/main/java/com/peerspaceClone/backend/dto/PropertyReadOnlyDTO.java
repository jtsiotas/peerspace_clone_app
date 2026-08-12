package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import com.peerspaceClone.backend.model.PropertyStatus;
import com.peerspaceClone.backend.model.PropertyType;

public record PropertyReadOnlyDTO(
    Long id,
    Long hostId,
    String title,
    String description,
    String city,
    String address,
    PropertyStatus status,
    BigDecimal hourlyRate,
    BigDecimal halfDayRate,
    String zip,
    String timezone,
    BigDecimal longitude,
    BigDecimal latitude,
    int sizeSqm,
    int capacity,
    int minHours,
    int maxHours,
    PropertyType type
) {
}
