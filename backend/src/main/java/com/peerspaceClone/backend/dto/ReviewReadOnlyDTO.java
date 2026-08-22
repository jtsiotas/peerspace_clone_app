package com.peerspaceClone.backend.dto;

public record ReviewReadOnlyDTO(
    Long id,
    Long bookingId,
    Long reviewerId,
    String reviewerUsername,
    Long revieweeId,
    String reviewerRole,
    int rating,
    String comment,
    Boolean isPublic
) {
}
