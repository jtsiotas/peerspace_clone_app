package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentReadOnlyDTO(
    Long id,
    Long bookingId,
    BigDecimal amount,
    String currency,
    String status,
    String method,
    LocalDateTime paidAt,
    LocalDateTime refundedAt,
    BigDecimal refundAmount
) {
}
