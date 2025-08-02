package com.stride.stride_common.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Base exception for all Stride platform exceptions.
 * Uses sealed classes for type safety and structured error handling.
 */
@Getter
public sealed class StrideException extends RuntimeException
    permits AuthenticationException, AuthorizationException, ValidationException, 
            BusinessException, ServiceException {
    
    private final String errorCode;
    private final Map<String, Object> context;
    private final Throwable rootCause;
    
    protected StrideException(String message, String errorCode) {
        this(message, errorCode, new HashMap<>(), null);
    }
    
    protected StrideException(String message, String errorCode, Map<String, Object> context) {
        this(message, errorCode, context, null);
    }
    
    protected StrideException(String message, String errorCode, Throwable cause) {
        this(message, errorCode, new HashMap<>(), cause);
    }
    
    protected StrideException(String message, String errorCode, Map<String, Object> context, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = new HashMap<>(context);
        this.rootCause = cause;
    }
    
    /**
     * Add context information to the exception
     */
    public StrideException withContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
}

