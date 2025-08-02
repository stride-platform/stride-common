package com.stride.stride_common.exceptions;

import lombok.Getter;

/**
 * Authentication-related exceptions
 */
@Getter
public final class AuthenticationException extends StrideException {
    
    public AuthenticationException(String message) {
        super(message, "AUTH_ERROR");
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, "AUTH_ERROR", cause);
    }
    
    // Specific authentication error types
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Invalid username or password");
    }
    
    public static AuthenticationException tokenExpired() {
        return new AuthenticationException("Authentication token has expired");
    }
    
    public static AuthenticationException invalidToken() {
        return new AuthenticationException("Invalid authentication token");
    }
    
    public static AuthenticationException missingToken() {
        return new AuthenticationException("Authentication token is required");
    }
}
