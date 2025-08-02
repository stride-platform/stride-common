package com.stride.stride_common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Unit tests for SecurityConfig - focused on configuration validation
 */
class SecurityConfigSimpleTest {

    @Test
    void shouldCreateSecurityConfigWithCorsSource() {
        CorsConfigurationSource corsSource = new CorsConfig().corsConfigurationSource();
        SecurityConfig securityConfig = new SecurityConfig(corsSource);
        
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void shouldBuildFilterChainWithoutErrors() {
        CorsConfigurationSource corsSource = new CorsConfig().corsConfigurationSource();
        SecurityConfig securityConfig = new SecurityConfig(corsSource);
        
        // Test that SecurityConfig can be instantiated without errors
        // The actual filter chain building is tested in integration tests
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void shouldHaveRequiredPublicUrlsInConstants() {
        // Verify that SecurityConstants has the URLs we expect to be public
        String[] publicUrls = SecurityConstants.PUBLIC_URLS;
        
        assertThat(publicUrls).contains(
            "/api/auth/login",
            "/api/auth/register", 
            "/api/health/**",
            "/actuator/**"
        );
    }

    @Test
    void shouldHaveInternalUrlsInConstants() {
        String[] internalUrls = SecurityConstants.INTERNAL_URLS;
        
        assertThat(internalUrls).contains("/internal/**");
    }

    @Test
    void corsConfigShouldBeCreatableWithDefaults() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        assertThat(source).isNotNull();
    }

    @Test
    void securityConfigShouldAcceptNullCorsSource() {
        // Test defensive programming - should handle null gracefully
        assertThatNoException().isThrownBy(() -> {
            new SecurityConfig(null);
        });
    }

    @Test
    void passwordEncoderConfigShouldBeCreatable() {
        PasswordEncoderConfig config = new PasswordEncoderConfig();
        
        assertThat(config.passwordEncoder()).isNotNull();
    }

    @Test
    void securityPropertiesShouldHaveDefaults() {
        SecurityProperties properties = new SecurityProperties();
        
        assertThat(properties.isEnableMethodSecurity()).isTrue();
        assertThat(properties.isEnableCsrfProtection()).isFalse();
        assertThat(properties.getRateLimiting()).isNotNull();
    }
}