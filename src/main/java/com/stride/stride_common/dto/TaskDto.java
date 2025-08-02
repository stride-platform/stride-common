package com.stride.stride_common.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard Task DTO for cross-service communication
 */
public record TaskDto(
    String id,
    String title,
    String description,
    String status,
    String priority,
    String assigneeId,
    String creatorId,
    String teamId,        // Added team context
    String projectId,     // Project grouping
    List<String> tags,
    Instant dueDate,
    Instant createdAt,
    Instant updatedAt
) {
    // Compact constructor for validation
    public TaskDto {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task ID cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be null or blank");
        }
    }
}