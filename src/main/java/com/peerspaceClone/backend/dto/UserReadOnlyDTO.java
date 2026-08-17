package com.peerspaceClone.backend.dto;

import java.util.Set;
import java.util.UUID;
public record UserReadOnlyDTO(
    Long id,
    UUID uuid,
    String username,
    String email,
    String firstName,
    String lastName,
    Set<String> roles
) {
    public UUID getUuid() {
        return uuid;
    }
}
