package com.stride.stride_common.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Business logic exceptions
 */
@Getter
public final class BusinessException extends StrideException {
    
    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    public BusinessException(String message, String errorCode, Map<String, Object> context) {
        super(message, errorCode, context);
    }
    
    // Common business exceptions
    public static BusinessException resourceNotFound(String resourceType, String resourceId) {
        Map<String, Object> context = new HashMap<>();
        context.put("resourceType", resourceType);
        context.put("resourceId", resourceId);
        return new BusinessException("Resource not found", "RESOURCE_NOT_FOUND", context);
    }
    
    public static BusinessException resourceAlreadyExists(String resourceType, String identifier) {
        Map<String, Object> context = new HashMap<>();
        context.put("resourceType", resourceType);
        context.put("identifier", identifier);
        return new BusinessException("Resource already exists", "RESOURCE_EXISTS", context);
    }
    
    public static BusinessException invalidOperation(String operation, String reason) {
        Map<String, Object> context = new HashMap<>();
        context.put("operation", operation);
        context.put("reason", reason);
        return new BusinessException("Invalid operation", "INVALID_OPERATION", context);
    }
    
    public static BusinessException businessRuleViolation(String rule, String details) {
        Map<String, Object> context = new HashMap<>();
        context.put("rule", rule);
        context.put("details", details);
        return new BusinessException("Business rule violation", "BUSINESS_RULE_VIOLATION", context);
    }
}