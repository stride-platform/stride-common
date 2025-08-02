package com.stride.stride_common.dto;

import java.time.Instant;

/**
 * Organization DTO for cross-service communication
 */
public record OrganizationDto(
    String id,
    String name,
    String domain,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
    // Compact constructor for validation
    public OrganizationDto {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Organization ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Organization name cannot be null or blank");
        }
    }
}