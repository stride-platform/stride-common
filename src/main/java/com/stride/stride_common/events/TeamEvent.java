package com.stride.stride_common.events;

import java.time.Instant;

public sealed interface TeamEvent extends BaseEvent
     permits TeamCreatedEvent, TeamMemberAddedEvent   {

}

record TeamCreatedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String teamId,
    String organizationId,
    String teamName,
    String createdBy
) implements TeamEvent {

    
    public String getEventType() {
        return "TEAM_CREATED";
    }

    
    public String getAggregateId() {
        return teamId;
    }
}

/**
 * Event fired when a member is added to a team
 */
record TeamMemberAddedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String teamId,
    String userId,
    String role,
    String addedBy
) implements TeamEvent {

    
    public String getEventType() {
        return "TEAM_MEMBER_ADDED";
    }

    
    public String getAggregateId() {
        return teamId;
    }
}

// Note: The above code assumes that the BaseEvent interface has methods getEventType() and getAggregateId() defined.
// If these methods are not present in BaseEvent, they should be added accordingly.
