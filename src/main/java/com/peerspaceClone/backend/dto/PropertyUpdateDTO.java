package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import com.peerspaceClone.backend.model.PropertyStatus;
import com.peerspaceClone.backend.model.PropertyType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PropertyUpdateDTO(
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    String title,

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    String description,

    @NotNull(message = "Property status is required")
    PropertyStatus status,

    @NotNull(message = "Hourly rate is required")
    @Positive(message = "Hourly rate must be positive")
    BigDecimal hourlyRate,

    @NotNull(message = "Half-day rate is required")
    @Positive(message = "Half-day rate must be positive")
    BigDecimal halfDayRate,

    @Min(value = 2, message = "Capacity must be at least 2 people")
    @Max(value = 10, message = "Capacity cannot exceed 10 people")
    int capacity,

    @Min(value = 6, message = "Minimum rental duration must be at least 6 hours")
    int minHours,

    @Positive(message = "Maximum rental duration must be positive")
    int maxHours,

    @NotNull(message = "Property type is required")
    PropertyType type
) {
}