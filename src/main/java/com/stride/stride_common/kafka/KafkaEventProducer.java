package com.stride.stride_common.kafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.stride.stride_common.events.BaseEvent;
import com.stride.stride_common.exceptions.EventPublishException;

import lombok.extern.slf4j.Slf4j;


/**
 * Kafka implementation of EventPublisher
 */
@Service
@Slf4j
public class KafkaEventProducer implements EventPublisher {
    
    
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventSerializer eventSerializer;
    private final KafkaTopicResolver topicResolver;
    
    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate, 
                             EventSerializer eventSerializer,
                             KafkaTopicResolver topicResolver) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventSerializer = eventSerializer;
        this.topicResolver = topicResolver;
    }
    
    @Override
    public CompletableFuture<Void> publish(BaseEvent event) {
        String topic = topicResolver.resolveTopicFor(event);
        return publish(topic, event);
    }
    
    @Override
    public CompletableFuture<Void> publish(String topic, BaseEvent event) {
        return publish(topic, null, event);
    }
    
    @Override
    public CompletableFuture<Void> publish(String topic, String key, BaseEvent event) {
        try {
            String serializedEvent = eventSerializer.serialize(event);
            String partitionKey = key != null ? key : generatePartitionKey(event);
            
            log.debug("Publishing event {} to topic {} with key {}", 
                event.getClass().getSimpleName(), topic, partitionKey);
            
            CompletableFuture<SendResult<String, Object>> sendFuture = 
                kafkaTemplate.send(topic, partitionKey, serializedEvent);
            
            return sendFuture
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        String errorMsg = String.format("Failed to publish event %s to topic %s", 
                            event.getClass().getSimpleName(), topic);
                        log.error(errorMsg, throwable);
                        throw new RuntimeException(new EventPublishException(errorMsg, topic, event.getClass().getSimpleName(), throwable));
                    } else {
                        log.info("Successfully published event {} to topic {} partition {} offset {}", 
                            event.getClass().getSimpleName(), topic, 
                            result.getRecordMetadata().partition(), 
                            result.getRecordMetadata().offset());
                        return (Void) null;
                    }
                });
                
        } catch (Exception e) {
            String errorMsg = String.format("Error serializing or publishing event %s to topic %s", 
                event.getClass().getSimpleName(), topic);
            log.error(errorMsg, e);
            
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(
                new EventPublishException(errorMsg, topic, event.getClass().getSimpleName(), e));
            return failedFuture;
        }
    }
    
    @Override
    public void publishSync(BaseEvent event) {
        String topic = topicResolver.resolveTopicFor(event);
        publishSync(topic, event);
    }
    
    @Override
    public void publishSync(String topic, BaseEvent event) {
        try {
            // Use the async method but wait for completion with timeout
            CompletableFuture<Void> future = publish(topic, event);
            future.get(30, TimeUnit.SECONDS); // 30 second timeout
            
        } catch (Exception e) {
            if (e instanceof EventPublishException) {
                throw (EventPublishException) e;
            }
            
            String errorMsg = String.format("Timeout or error publishing event %s synchronously to topic %s", 
                event.getClass().getSimpleName(), topic);
            throw new EventPublishException(errorMsg, topic, event.getClass().getSimpleName(), e);
        }
    }
    
    /**
     * Generate a partition key for the event to ensure proper partitioning
     */
    private String generatePartitionKey(BaseEvent event) {
        // Use event class name + hash for consistent partitioning
        // This ensures events of the same type go to same partition
        return event.getClass().getSimpleName() + "-" + Math.abs(event.hashCode() % 1000);
    }
}