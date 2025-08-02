package com.stride.stride_common.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard Team DTO for cross-service communication
 */
public record TeamDto(
    String id,
    String name,
    String description,
    String organizationId,
    List<TeamMemberDto> members,
    Instant createdAt,
    Instant updatedAt
) {
    // Compact constructor for validation
    public TeamDto {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Team ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or blank");
        }
    }
}

/**
 * Team member information
 */
record TeamMemberDto(
    String userId,
    String fullName,
    String email,
    String role,
    Instant joinedAt
) {
    // Compact constructor for validation
    public TeamMemberDto {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank");
        }
    }
}