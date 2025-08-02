package com.stride.stride_common.security;

/**
 * Common security constants used across all Stride services
 */
public final class SecurityConstants {
    
    // JWT Related
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_USER_ID_CLAIM = "userId";
    public static final String JWT_ORGANIZATION_ID_CLAIM = "organizationId";
    public static final String JWT_ROLES_CLAIM = "roles";
    public static final String JWT_TEAMS_CLAIM = "teams";
    
    // Roles
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_ORG_ADMIN = "ORG_ADMIN";
    public static final String ROLE_TEAM_ADMIN = "TEAM_ADMIN";
    public static final String ROLE_TEAM_MEMBER = "TEAM_MEMBER";
    
    // Permissions
    public static final String PERMISSION_CREATE_ORGANIZATION = "CREATE_ORGANIZATION";
    public static final String PERMISSION_MANAGE_USERS = "MANAGE_USERS";
    public static final String PERMISSION_MANAGE_TEAMS = "MANAGE_TEAMS";
    public static final String PERMISSION_CREATE_TASKS = "CREATE_TASKS";
    public static final String PERMISSION_ASSIGN_TASKS = "ASSIGN_TASKS";
    public static final String PERMISSION_VIEW_ALL_TASKS = "VIEW_ALL_TASKS";
    
    // API Paths
    public static final String[] PUBLIC_URLS = {
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/refresh",
        "/api/health/**",
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };
    
    public static final String[] INTERNAL_URLS = {
        "/internal/**"
    };
    
    // CORS
    public static final String[] ALLOWED_ORIGINS = {
        "http://localhost:3000",  // React dev server
        "http://localhost:5173",  // Vite dev server
        "https://app.stride.com", // Production frontend
        "https://staging.stride.com" // Staging frontend
    };
    
    public static final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    };
    
    public static final String[] ALLOWED_HEADERS = {
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "Accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"
    };
    
    // Security Headers
    public static final String ORGANIZATION_HEADER = "X-Organization-Id";
    public static final String USER_CONTEXT_HEADER = "X-User-Context";
    
    private SecurityConstants() {
        // Utility class
    }
}