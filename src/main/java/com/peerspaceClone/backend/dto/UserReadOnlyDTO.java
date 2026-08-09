package com.peerspaceClone.backend.dto;

import java.util.Set;
import java.util.UUID;
import com.peerspaceClone.backend.model.Role;

public record UserReadOnlyDTO(
    Long id,
    UUID uuid,
    String username,
    String email,
    String firstName,
    String lastName,
    Set<Role> roles
) {
    public UUID getUuid() {
        return uuid;
    }
}
