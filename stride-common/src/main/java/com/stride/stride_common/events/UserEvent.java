package com.stride.stride_common.events;

import java.time.Instant;

public sealed interface UserEvent extends BaseEvent permits UserCreatedEvent, UserUpdatedEvent {

}

/**
 * Event fired when a user is created
 */
 record UserCreatedEvent(
   String eventId,
    String correlationId,
    Instant timestamp,
    String userId,
    String organizationId,
    String email,
    String fullName
    ) 

implements UserEvent {
    
    public String getEventType() {
        return "USER_CREATED";
    }
    
    public String getAggregateId() {
        return userId;
    }
}

/**
 * Event fired when a user is updated
 */
 record UserUpdatedEvent(
    String eventId,
    String correlationId,
    Instant timestamp,
    String userId,
    String organizationId,
    String email,
    String fullName
) implements UserEvent {
    
    
    public String getEventType() {
        return "USER_UPDATED";
    }
    
    
    public String getAggregateId() {
        return userId;
    }
}




