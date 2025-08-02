package com.stride.stride_common.exceptions;

/**
 * Exception thrown when event serialization/deserialization fails
 */
public final class EventSerializationException extends StrideException {
    
    public EventSerializationException(String message) {
        super("EVENT_SERIALIZATION_FAILED", message);
    }
    
    public EventSerializationException(String message, Throwable cause) {
        super("EVENT_SERIALIZATION_FAILED", message, cause);
    }
}