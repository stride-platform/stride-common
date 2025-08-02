package com.stride.stride_common.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard User DTO for cross-service communication
 */
public record UserDto(
    String id,
    String email,
    String fullName,
    String organizationId,
    List<String> roles,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
    // Compact constructor for validation
    public UserDto {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}