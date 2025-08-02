package com.stride.stride_common.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error response for detailed error information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String error;
    private String errorCode;
    private String message;
    private String path;
    private Instant timestamp;
    private Map<String, String> fieldErrors;
    private Map<String, Object> context;
    
    public static ErrorResponse of(String error, String errorCode, String message) {
        ErrorResponse response = new ErrorResponse();
        response.error = error;
        response.errorCode = errorCode;
        response.message = message;
        response.timestamp = Instant.now();
        return response;
    }
    
    public static ErrorResponse withFieldErrors(String error, String errorCode, String message, 
                                              Map<String, String> fieldErrors) {
        ErrorResponse response = new ErrorResponse();
        response.error = error;
        response.errorCode = errorCode;
        response.message = message;
        response.fieldErrors = fieldErrors;
        response.timestamp = Instant.now();
        return response;
    }
}