package com.stride.stride_common.exceptions;



/**
 * Exception thrown when event publishing fails
 */
public final class EventPublishException extends StrideException {
    
    private final String topic;
    private final String eventType;
    
    public EventPublishException(String message, String topic, String eventType) {
        super("EVENT_PUBLISH_FAILED", message);
        this.topic = topic;
        this.eventType = eventType;
    }
    
    public EventPublishException(String message, String topic, String eventType, Throwable cause) {
        super("EVENT_PUBLISH_FAILED", message, cause);
        this.topic = topic;
        this.eventType = eventType;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    @Override
    public String toString() {
        return String.format("EventPublishException{topic='%s', eventType='%s', message='%s'}", 
            topic, eventType, getMessage());
    }
}