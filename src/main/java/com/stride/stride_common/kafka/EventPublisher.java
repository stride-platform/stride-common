package com.stride.stride_common.kafka;
import java.util.concurrent.CompletableFuture;

import com.stride.stride_common.events.BaseEvent;
import com.stride.stride_common.exceptions.EventPublishException;

/**
 * Interface for publishing events to Kafka topics
 */
public interface EventPublisher {
    
    /**
     * Publish an event asynchronously
     * 
     * @param event the base event to publish
     * @return CompletableFuture that completes when the event is sent
     */
    CompletableFuture<Void> publish(BaseEvent event);
    
    /**
     * Publish an event to a specific topic asynchronously
     * 
     * @param topic the Kafka topic name
     * @param event the base event to publish
     * @return CompletableFuture that completes when the event is sent
     */
    CompletableFuture<Void> publish(String topic, BaseEvent event);
    
    /**
     * Publish an event with a specific partition key
     * 
     * @param topic the Kafka topic name
     * @param key the partition key (e.g., organizationId, userId)
     * @param event the base event to publish
     * @return CompletableFuture that completes when the event is sent
     */
    CompletableFuture<Void> publish(String topic, String key, BaseEvent event);
    
    /**
     * Publish an event synchronously (blocks until sent)
     * Use sparingly - prefer async methods for better performance
     * 
     * @param event the base event to publish
     * @throws EventPublishException if publishing fails
     */
    void publishSync(BaseEvent event);
    
    /**
     * Publish an event synchronously to a specific topic
     * 
     * @param topic the Kafka topic name
     * @param event the base event to publish
     * @throws EventPublishException if publishing fails
     */
    void publishSync(String topic, BaseEvent event);
}