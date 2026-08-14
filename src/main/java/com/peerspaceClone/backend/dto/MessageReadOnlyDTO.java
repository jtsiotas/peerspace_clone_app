package com.peerspaceClone.backend.dto;

import java.time.Instant;

public record MessageReadOnlyDTO(
    Long id,
    Long bookingId,
    Long senderId,
    String senderUsername,
    String content,
    Instant createdAt
) {
}
