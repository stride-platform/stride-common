package com.stride.stride_common.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Validation-related exceptions
 */
@Getter
public final class ValidationException extends StrideException {
    
    private final Map<String, String> fieldErrors;
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = new HashMap<>();
    }
    
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = new HashMap<>(fieldErrors);
    }
    
    public ValidationException addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
        return this;
    }
    
    // Specific validation error types
    public static ValidationException requiredField(String fieldName) {
        return new ValidationException("Required field is missing")
            .addFieldError(fieldName, "This field is required");
    }
    
    public static ValidationException invalidFormat(String fieldName, String expectedFormat) {
        return new ValidationException("Field format is invalid")
            .addFieldError(fieldName, "Expected format: " + expectedFormat);
    }
    
    public static ValidationException invalidValue(String fieldName, String reason) {
        return new ValidationException("Field value is invalid")
            .addFieldError(fieldName, reason);
    }
}