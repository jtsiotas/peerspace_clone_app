package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentInsertDTO(
    @NotNull(message = "Booking ID is required")
    Long bookingId,

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than zero")
    BigDecimal amount,

    @NotBlank(message = "Currency is required")
    String currency,

    @NotBlank(message = "Payment method is required")
    String method
) {
}
