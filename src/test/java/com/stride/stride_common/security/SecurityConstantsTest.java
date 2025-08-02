package com.stride.stride_common.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 * Tests for SecurityConstants to ensure values are consistent and complete
 */
class SecurityConstantsTest {

    @Test
    void shouldHaveCorrectJwtConstants() {
        assertThat(SecurityConstants.JWT_TOKEN_HEADER).isEqualTo("Authorization");
        assertThat(SecurityConstants.JWT_TOKEN_PREFIX).isEqualTo("Bearer ");
        assertThat(SecurityConstants.JWT_USER_ID_CLAIM).isEqualTo("userId");
        assertThat(SecurityConstants.JWT_ORGANIZATION_ID_CLAIM).isEqualTo("organizationId");
        assertThat(SecurityConstants.JWT_ROLES_CLAIM).isEqualTo("roles");
        assertThat(SecurityConstants.JWT_TEAMS_CLAIM).isEqualTo("teams");
    }

    @Test
    void shouldHaveAllRequiredRoles() {
        assertThat(SecurityConstants.ROLE_SUPER_ADMIN).isEqualTo("SUPER_ADMIN");
        assertThat(SecurityConstants.ROLE_ORG_ADMIN).isEqualTo("ORG_ADMIN");
        assertThat(SecurityConstants.ROLE_TEAM_ADMIN).isEqualTo("TEAM_ADMIN");
        assertThat(SecurityConstants.ROLE_TEAM_MEMBER).isEqualTo("TEAM_MEMBER");
    }

    @Test
    void shouldHaveAllRequiredPermissions() {
        assertThat(SecurityConstants.PERMISSION_CREATE_ORGANIZATION).isEqualTo("CREATE_ORGANIZATION");
        assertThat(SecurityConstants.PERMISSION_MANAGE_USERS).isEqualTo("MANAGE_USERS");
        assertThat(SecurityConstants.PERMISSION_MANAGE_TEAMS).isEqualTo("MANAGE_TEAMS");
        assertThat(SecurityConstants.PERMISSION_CREATE_TASKS).isEqualTo("CREATE_TASKS");
        assertThat(SecurityConstants.PERMISSION_ASSIGN_TASKS).isEqualTo("ASSIGN_TASKS");
        assertThat(SecurityConstants.PERMISSION_VIEW_ALL_TASKS).isEqualTo("VIEW_ALL_TASKS");
    }

    @Test
    void shouldHavePublicUrlsIncludingHealthAndAuth() {
        String[] publicUrls = SecurityConstants.PUBLIC_URLS;
        
        assertThat(publicUrls).contains(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/api/health/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
        );
        
        assertThat(publicUrls).hasSize(7);
    }

    @Test
    void shouldHaveInternalUrls() {
        String[] internalUrls = SecurityConstants.INTERNAL_URLS;
        
        assertThat(internalUrls).contains("/internal/**");
        assertThat(internalUrls).hasSize(1);
    }

    @Test
    void shouldHaveAllowedOriginsForDevelopmentAndProduction() {
        String[] allowedOrigins = SecurityConstants.ALLOWED_ORIGINS;
        
        assertThat(allowedOrigins).contains(
            "http://localhost:3000",      // React dev
            "http://localhost:5173",      // Vite dev
            "https://app.stride.com",     // Production
            "https://staging.stride.com"  // Staging
        );
        
        assertThat(allowedOrigins).hasSize(4);
    }

    @Test
    void shouldHaveStandardHttpMethods() {
        String[] allowedMethods = SecurityConstants.ALLOWED_METHODS;
        
        assertThat(allowedMethods).contains(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        );
        
        assertThat(allowedMethods).hasSize(6);
    }

    @Test
    void shouldHaveCommonRequestHeaders() {
        String[] allowedHeaders = SecurityConstants.ALLOWED_HEADERS;
        
        assertThat(allowedHeaders).contains(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        );
        
        assertThat(allowedHeaders).hasSize(7);
    }

    @Test
    void shouldHaveCustomHeaders() {
        assertThat(SecurityConstants.ORGANIZATION_HEADER).isEqualTo("X-Organization-Id");
        assertThat(SecurityConstants.USER_CONTEXT_HEADER).isEqualTo("X-User-Context");
    }

    @Test
    void shouldNotBeInstantiable() {
        // SecurityConstants should be a utility class with private constructor
        // This test ensures it follows proper utility class pattern
        assertThat(SecurityConstants.class.getDeclaredConstructors()).hasSize(1);
        assertThat(SecurityConstants.class.getDeclaredConstructors()[0].canAccess(null)).isFalse();
    }
}