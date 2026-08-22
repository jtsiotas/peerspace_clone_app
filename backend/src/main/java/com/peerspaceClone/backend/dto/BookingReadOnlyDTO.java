package com.peerspaceClone.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import com.peerspaceClone.backend.model.BookingStatus;

public record BookingReadOnlyDTO(
    Long id,
    Long propertyId,
    String propertyTitle,
    Long guestId,
    String guestUsername,
    Instant startDatetime,
    Instant endDatetime,
    BigDecimal totalHours,
    BigDecimal propertyRate,
    BigDecimal subtotal,
    BigDecimal hostFee,
    BigDecimal guestFee,
    BigDecimal totalAmount,
    BigDecimal hostPayout,
    BookingStatus status,
    String cancellationPolicy,
    String canceledBy,
    Instant cancellationDate,
    String cancelationReason
) {
}
