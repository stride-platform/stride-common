package com.stride.stride_common.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Authorization-related exceptions
 */
@Getter
public final class AuthorizationException extends StrideException {
    
    public AuthorizationException(String message) {
        super(message, "AUTHZ_ERROR");
    }
    
    public AuthorizationException(String message, Map<String, Object> context) {
        super(message, "AUTHZ_ERROR", context);
    }
    
    // Specific authorization error types
    public static AuthorizationException insufficientPermissions(String requiredRole) {
        Map<String, Object> context = new HashMap<>();
        context.put("requiredRole", requiredRole);
        return new AuthorizationException("Insufficient permissions", context);
    }
    
    public static AuthorizationException resourceAccessDenied(String resourceId, String resourceType) {
        Map<String, Object> context = new HashMap<>();
        context.put("resourceId", resourceId);
        context.put("resourceType", resourceType);
        return new AuthorizationException("Access denied to resource", context);
    }
    
    public static AuthorizationException organizationAccessDenied(String organizationId) {
        Map<String, Object> context = new HashMap<>();
        context.put("organizationId", organizationId);
        return new AuthorizationException("Access denied to organization", context);
    }
}