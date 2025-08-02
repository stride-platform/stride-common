package com.stride.stride_common.events;

import java.time.Instant;

public sealed interface TaskEvent extends BaseEvent 
permits TaskCreatedEvent, TaskUpdatedEvent, TaskAssignedEvent {

}

/**
 * Event fired when a task is created
 */
record TaskCreatedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String taskId,
    String teamId,
    String projectId,
    String title,
    String assignedTo,
    String createdBy,
    String organizationId
) implements TaskEvent {

    
    public String getEventType() {
        return "TASK_CREATED";
    }

    
    public String getAggregateId() {
        return taskId;
    }
}

/**
 * Event fired when a task is updated               
 */

record TaskUpdatedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String taskId,
    String teamId,
    String status,
    String previousStatus,
    String updatedBy
) implements TaskEvent {

    
    public String getEventType() {
        return "TASK_UPDATED";
    }

    
    public String getAggregateId() {
        return taskId;
    }
}

/**
 * Event fired when a task is assigned to a user
 */
record TaskAssignedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String taskId,
    String teamId,
    String assignedTo,
    String assignedBy,
    String previousAssignee
) implements TaskEvent {

    
    public String getEventType() {
        return "TASK_ASSIGNED";
    }

    
    public String getAggregateId() {
        return taskId;
    }
}

// Note: The above code assumes that the BaseEvent interface has methods getEventType() and getAggregateId() defined.
// If these methods are not present in BaseEvent, they should be added accordingly. 

