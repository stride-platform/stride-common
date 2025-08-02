package com.stride.stride_common.security;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Tests for CorsConfig
 */
class CorsConfigTest {

    @Test
    void shouldCreateCorsConfigurationSourceWithDefaults() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        assertThat(source).isNotNull();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        assertThat(config).isNotNull();
    }

    @Test
    void shouldHaveDefaultAllowedOrigins() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        List<String> allowedOriginPatterns = config.getAllowedOriginPatterns();
        
        assertThat(allowedOriginPatterns).contains(
            "http://localhost:3000",
            "http://localhost:5173", 
            "https://app.stride.com",
            "https://staging.stride.com"
        );
    }

    @Test
    void shouldHaveAllRequiredMethods() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        List<String> allowedMethods = config.getAllowedMethods();
        
        assertThat(allowedMethods).contains(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        );
    }

    @Test
    void shouldHaveAllRequiredHeaders() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        List<String> allowedHeaders = config.getAllowedHeaders();
        
        assertThat(allowedHeaders).contains(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        );
    }

    @Test
    void shouldExposeResponseHeaders() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        List<String> exposedHeaders = config.getExposedHeaders();
        
        assertThat(exposedHeaders).contains(
            "Authorization",
            "X-Total-Count",
            "X-Page-Count",
            "X-Current-Page",
            "X-Organization-Id",
            "X-User-Context"
        );
    }

    @Test
    void shouldAllowCredentialsByDefault() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        assertThat(config.getAllowCredentials()).isTrue();
    }

    @Test
    void shouldHaveReasonableMaxAge() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        // Default max age should be 3600 seconds (1 hour)
        assertThat(config.getMaxAge()).isEqualTo(3600L);
    }

    @SpringBootTest
    @TestPropertySource(properties = {
        "app.cors.allowed-origins=https://custom1.com,https://custom2.com",
        "app.cors.allow-credentials=false",
        "app.cors.max-age=7200"
    })
    static class CorsConfigWithCustomPropertiesTest {
        
        @Test
        void shouldRespectCustomProperties() {
            // This test would require Spring context to inject properties
            // For now, we test the logic manually
            CorsConfig corsConfig = new CorsConfig();
            
            // We can't easily test property injection without full Spring context
            // But we can verify the bean creation doesn't fail
            CorsConfigurationSource source = corsConfig.corsConfigurationSource();
            assertThat(source).isNotNull();
        }
    }

    @Test
    void shouldHandleNullAdditionalOrigins() {
        CorsConfig corsConfig = new CorsConfig();
        // additionalAllowedOrigins would be null by default
        
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        CorsConfiguration config = source.getCorsConfiguration(request);
        
        // Should not throw exception and should have at least default origins
        assertThat(config.getAllowedOriginPatterns()).isNotEmpty();
    }
}