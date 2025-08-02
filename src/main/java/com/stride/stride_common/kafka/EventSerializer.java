package com.stride.stride_common.kafka;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stride.stride_common.events.BaseEvent;
import com.stride.stride_common.exceptions.EventSerializationException;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON serializer for base events
 */
@Component
@Slf4j
public class EventSerializer {
    
    
    
    private final ObjectMapper objectMapper;
    
    public EventSerializer() {
        this.objectMapper = createObjectMapper();
    }
    
    /**
     * Serialize a base event to JSON string
     */
    public String serialize(BaseEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            log.debug("Serialized event {} to JSON: {}", event.getClass().getSimpleName(), json);
            return json;
            
        } catch (JsonProcessingException e) {
            String errorMsg = String.format("Failed to serialize event %s to JSON", event.getClass().getSimpleName());
            log.error(errorMsg, e);
            throw new EventSerializationException(errorMsg, e);
        }
    }
    
    /**
     * Deserialize JSON string to base event
     */
    public <T extends BaseEvent> T deserialize(String json, Class<T> eventClass) {
        try {
            T event = objectMapper.readValue(json, eventClass);
            log.debug("Deserialized JSON to event {}: {}", eventClass.getSimpleName(), json);
            return event;
            
        } catch (JsonProcessingException e) {
            String errorMsg = String.format("Failed to deserialize JSON to event %s", eventClass.getSimpleName());
            log.error(errorMsg, e);
            throw new EventSerializationException(errorMsg, e);
        }
    }
    
    /**
     * Create and configure ObjectMapper for event serialization
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register JavaTimeModule for Java 8 time support
        mapper.registerModule(new JavaTimeModule());
        
        // Configure for consistent serialization
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return mapper;
    }
    
    /**
     * Get the configured ObjectMapper (useful for testing)
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}