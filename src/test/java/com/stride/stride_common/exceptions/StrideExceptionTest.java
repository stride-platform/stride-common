package com.stride.stride_common.exceptions;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stride Exception Hierarchy Tests")
class StrideExceptionTest {

    @Test
    @DisplayName("AuthenticationException factory methods work correctly")
    void testAuthenticationExceptionFactoryMethods() {
        // Test invalidCredentials
        AuthenticationException invalidCreds = AuthenticationException.invalidCredentials();
        assertEquals("Invalid username or password", invalidCreds.getMessage());
        assertEquals("AUTH_ERROR", invalidCreds.getErrorCode());
        
        // Test tokenExpired
        AuthenticationException tokenExpired = AuthenticationException.tokenExpired();
        assertEquals("Authentication token has expired", tokenExpired.getMessage());
        assertEquals("AUTH_ERROR", tokenExpired.getErrorCode());
        
        // Test invalidToken
        AuthenticationException invalidToken = AuthenticationException.invalidToken();
        assertEquals("Invalid authentication token", invalidToken.getMessage());
        
        // Test missingToken
        AuthenticationException missingToken = AuthenticationException.missingToken();
        assertEquals("Authentication token is required", missingToken.getMessage());
    }

    @Test
    @DisplayName("AuthorizationException includes context information")
    void testAuthorizationExceptionWithContext() {
        // Test insufficientPermissions
        AuthorizationException exception = AuthorizationException.insufficientPermissions("ADMIN");
        
        assertEquals("Insufficient permissions", exception.getMessage());
        assertEquals("AUTHZ_ERROR", exception.getErrorCode());
        assertNotNull(exception.getContext());
        assertEquals("ADMIN", exception.getContext().get("requiredRole"));
    }

    @Test
    @DisplayName("ValidationException handles field errors correctly")
    void testValidationExceptionFieldErrors() {
        // Test requiredField
        ValidationException exception = ValidationException.requiredField("email");
        
        assertEquals("Required field is missing", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertTrue(exception.getFieldErrors().containsKey("email"));
        assertEquals("This field is required", exception.getFieldErrors().get("email"));
    }

    @Test
    @DisplayName("ValidationException can chain field errors")
    void testValidationExceptionChaining() {
        ValidationException exception = ValidationException.requiredField("email")
            .addFieldError("password", "Password too weak");
        
        assertEquals(2, exception.getFieldErrors().size());
        assertEquals("This field is required", exception.getFieldErrors().get("email"));
        assertEquals("Password too weak", exception.getFieldErrors().get("password"));
    }

    @Test
    @DisplayName("BusinessException factory methods include context")
    void testBusinessExceptionFactoryMethods() {
        // Test resourceNotFound
        BusinessException notFound = BusinessException.resourceNotFound("User", "user123");
        
        assertEquals("Resource not found", notFound.getMessage());
        assertEquals("RESOURCE_NOT_FOUND", notFound.getErrorCode());
        assertEquals("User", notFound.getContext().get("resourceType"));
        assertEquals("user123", notFound.getContext().get("resourceId"));
        
        // Test resourceAlreadyExists
        BusinessException alreadyExists = BusinessException.resourceAlreadyExists("User", "john@example.com");
        
        assertEquals("Resource already exists", alreadyExists.getMessage());
        assertEquals("RESOURCE_EXISTS", alreadyExists.getErrorCode());
        assertEquals("User", alreadyExists.getContext().get("resourceType"));
        assertEquals("john@example.com", alreadyExists.getContext().get("identifier"));
    }

    @Test
    @DisplayName("ServiceException handles infrastructure errors with causes")
    void testServiceExceptionWithCauses() {
        RuntimeException cause = new RuntimeException("Database connection failed");
        
        // Test databaseError
        ServiceException dbError = ServiceException.databaseError(cause);
        
        assertEquals("Database operation failed", dbError.getMessage());
        assertEquals("DATABASE_ERROR", dbError.getErrorCode());
        assertEquals(cause, dbError.getRootCause());
        
        // Test externalServiceError
        ServiceException serviceError = ServiceException.externalServiceError("PaymentService", cause);
        
        assertEquals("External service error", serviceError.getMessage());
        assertEquals("EXTERNAL_SERVICE_ERROR", serviceError.getErrorCode());
        assertEquals("PaymentService", serviceError.getContext().get("serviceName"));
        assertEquals(cause, serviceError.getRootCause());
    }

    @Test
    @DisplayName("Exception hierarchy maintains sealed class contracts")
    void testSealedClassHierarchy() {
        // Test that all exception types extend StrideException
        assertTrue(AuthenticationException.invalidCredentials() instanceof StrideException);
        assertTrue(AuthorizationException.insufficientPermissions("ADMIN") instanceof StrideException);
        assertTrue(ValidationException.requiredField("email") instanceof StrideException);
        assertTrue(BusinessException.resourceNotFound("User", "123") instanceof StrideException);
        assertTrue(ServiceException.databaseError(new RuntimeException()) instanceof StrideException);
    }

    @Test
    @DisplayName("Context information can be added dynamically")
    void testDynamicContextAddition() {
        BusinessException exception = BusinessException.resourceNotFound("Task", "task123");
        
        // Add additional context
        exception.withContext("userId", "user456");
        exception.withContext("organizationId", "org789");
        
        Map<String, Object> context = exception.getContext();
        assertEquals("Task", context.get("resourceType"));
        assertEquals("task123", context.get("resourceId"));
        assertEquals("user456", context.get("userId"));
        assertEquals("org789", context.get("organizationId"));
    }

    @Test
    @DisplayName("Exception messages and codes are consistent")
    void testExceptionConsistency() {
        // Verify that similar operations have consistent error codes
        assertEquals("AUTH_ERROR", AuthenticationException.invalidCredentials().getErrorCode());
        assertEquals("AUTH_ERROR", AuthenticationException.tokenExpired().getErrorCode());
        
        assertEquals("AUTHZ_ERROR", AuthorizationException.insufficientPermissions("ADMIN").getErrorCode());
        assertEquals("AUTHZ_ERROR", AuthorizationException.resourceAccessDenied("123", "Task").getErrorCode());
        
        assertEquals("VALIDATION_ERROR", ValidationException.requiredField("email").getErrorCode());
        assertEquals("VALIDATION_ERROR", ValidationException.invalidFormat("date", "yyyy-mm-dd").getErrorCode());
    }
}