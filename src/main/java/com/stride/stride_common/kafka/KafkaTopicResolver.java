package com.stride.stride_common.kafka;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stride.stride_common.events.BaseEvent;
import com.stride.stride_common.events.TaskEvent;
import com.stride.stride_common.events.TeamEvent;
import com.stride.stride_common.events.UserEvent;

/**
 * Resolves Kafka topic names for different event types
 * Supports environment-specific topic prefixes
 */
@Component
public class KafkaTopicResolver {
    
    @Value("${spring.kafka.topic.prefix:stride}")
    private String topicPrefix;
    
    @Value("${spring.profiles.active:local}")
    private String environment;
    
    private final Map<Class<? extends BaseEvent>, String> topicMappings;
    
    public KafkaTopicResolver() {
        this.topicMappings = initializeTopicMappings();
    }
    
    /**
     * Resolve the topic name for a given event
     */
    public String resolveTopicFor(BaseEvent event) {
        String baseTopic = topicMappings.get(event.getClass());
        
        if (baseTopic == null) {
            // Fallback: use the class name as topic name
            baseTopic = event.getClass().getSimpleName().toLowerCase().replace("event", "-events");
        }
        
        return buildTopicName(baseTopic);
    }
    
    /**
     * Resolve topic name for a specific event class
     */
    public String resolveTopicFor(Class<? extends BaseEvent> eventClass) {
        String baseTopic = topicMappings.get(eventClass);
        
        if (baseTopic == null) {
            // Fallback: use class name
            baseTopic = eventClass.getSimpleName().toLowerCase().replace("event", "-events");
        }
        
        return buildTopicName(baseTopic);
    }
    
    /**
     * Get topic name for user events
     */
    public String getUserEventsTopic() {
        return buildTopicName("user-events");
    }
    
    /**
     * Get topic name for task events
     */
    public String getTaskEventsTopic() {
        return buildTopicName("task-events");
    }
    
    /**
     * Get topic name for team events
     */
    public String getTeamEventsTopic() {
        return buildTopicName("team-events");
    }
    
    /**
     * Build the full topic name with prefix and environment
     */
    private String buildTopicName(String baseTopic) {
        return String.format("%s.%s.%s", topicPrefix, environment, baseTopic);
    }
    
    /**
     * Initialize mappings between event classes and their topics
     */
    private Map<Class<? extends BaseEvent>, String> initializeTopicMappings() {
        Map<Class<? extends BaseEvent>, String> mappings = new HashMap<>();
        
        // User events
        mappings.put(UserEvent.class, "user-events");
        
        // Task events  
        mappings.put(TaskEvent.class, "task-events");
        
        // Team events
        mappings.put(TeamEvent.class, "team-events");
        
        return mappings;
    }
    
    /**
     * Get all topic names (useful for administrative purposes)
     */
    public Map<String, String> getAllTopics() {
        Map<String, String> topics = new HashMap<>();
        
        topics.put("user-events", getUserEventsTopic());
        topics.put("task-events", getTaskEventsTopic());
        topics.put("team-events", getTeamEventsTopic());
        
        return topics;
    }
}