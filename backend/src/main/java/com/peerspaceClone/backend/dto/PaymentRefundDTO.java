package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRefundDTO(
    @NotNull(message = "Refund amount is required")
    @Positive(message = "Refund amount must be greater than zero")
    BigDecimal refundAmount
) {
}
