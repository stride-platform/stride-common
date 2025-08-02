package com.stride.stride_common.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Service-level exceptions (infrastructure issues)
 */
@Getter
public final class ServiceException extends StrideException {
    
    public ServiceException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
    
    public ServiceException(String message, String errorCode, Map<String, Object> context, Throwable cause) {
        super(message, errorCode, context, cause);
    }
    
    // Common service exceptions
    public static ServiceException databaseError(Throwable cause) {
        return new ServiceException("Database operation failed", "DATABASE_ERROR", cause);
    }
    
    public static ServiceException externalServiceError(String serviceName, Throwable cause) {
        Map<String, Object> context = new HashMap<>();
        context.put("serviceName", serviceName);
        return new ServiceException("External service error", "EXTERNAL_SERVICE_ERROR", context, cause);
    }
    
    public static ServiceException kafkaError(String operation, Throwable cause) {
        Map<String, Object> context = new HashMap<>();
        context.put("operation", operation);
        return new ServiceException("Kafka operation failed", "KAFKA_ERROR", context, cause);
    }
    
    public static ServiceException configurationError(String configKey) {
        Map<String, Object> context = new HashMap<>();
        context.put("configurationKey", configKey);
        return new ServiceException("Configuration error", "CONFIG_ERROR", context, null);
    }
}