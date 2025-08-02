package com.stride.stride_common.security;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 * Tests for SecurityProperties configuration class
 */
class SecurityPropertiesTest {

    @Test
    void shouldHaveDefaultValues() {
        SecurityProperties properties = new SecurityProperties();
        
        assertThat(properties.isEnableMethodSecurity()).isTrue();
        assertThat(properties.isEnableCsrfProtection()).isFalse();
        assertThat(properties.getAdditionalPublicUrls()).isEmpty();
        assertThat(properties.getRateLimiting()).isNotNull();
    }

    @Test
    void shouldAllowMethodSecurityToBeDisabled() {
        SecurityProperties properties = new SecurityProperties();
        
        properties.setEnableMethodSecurity(false);
        
        assertThat(properties.isEnableMethodSecurity()).isFalse();
    }

    @Test
    void shouldAllowCsrfProtectionToBeEnabled() {
        SecurityProperties properties = new SecurityProperties();
        
        properties.setEnableCsrfProtection(true);
        
        assertThat(properties.isEnableCsrfProtection()).isTrue();
    }

    @Test
    void shouldAllowAdditionalPublicUrls() {
        SecurityProperties properties = new SecurityProperties();
        List<String> additionalUrls = List.of("/api/public/**", "/webhook/**");
        
        properties.setAdditionalPublicUrls(additionalUrls);
        
        assertThat(properties.getAdditionalPublicUrls()).containsExactly("/api/public/**", "/webhook/**");
    }

    @Test
    void shouldHaveRateLimitingDefaults() {
        SecurityProperties properties = new SecurityProperties();
        SecurityProperties.RateLimiting rateLimiting = properties.getRateLimiting();
        
        assertThat(rateLimiting.isEnabled()).isTrue();
        assertThat(rateLimiting.getRequestsPerMinute()).isEqualTo(100);
        assertThat(rateLimiting.getBurstCapacity()).isEqualTo(200);
    }

    @Test
    void shouldAllowRateLimitingConfiguration() {
        SecurityProperties properties = new SecurityProperties();
        SecurityProperties.RateLimiting rateLimiting = properties.getRateLimiting();
        
        rateLimiting.setEnabled(false);
        rateLimiting.setRequestsPerMinute(50);
        rateLimiting.setBurstCapacity(100);
        
        assertThat(rateLimiting.isEnabled()).isFalse();
        assertThat(rateLimiting.getRequestsPerMinute()).isEqualTo(50);
        assertThat(rateLimiting.getBurstCapacity()).isEqualTo(100);
    }

    @Test
    void shouldAllowRateLimitingToBeReplaced() {
        SecurityProperties properties = new SecurityProperties();
        SecurityProperties.RateLimiting newRateLimiting = new SecurityProperties.RateLimiting();
        
        newRateLimiting.setEnabled(false);
        newRateLimiting.setRequestsPerMinute(25);
        newRateLimiting.setBurstCapacity(50);
        
        properties.setRateLimiting(newRateLimiting);
        
        SecurityProperties.RateLimiting result = properties.getRateLimiting();
        assertThat(result.isEnabled()).isFalse();
        assertThat(result.getRequestsPerMinute()).isEqualTo(25);
        assertThat(result.getBurstCapacity()).isEqualTo(50);
    }

    @Test
    void rateLimitingShouldBeIndependent() {
        SecurityProperties properties1 = new SecurityProperties();
        SecurityProperties properties2 = new SecurityProperties();
        
        properties1.getRateLimiting().setRequestsPerMinute(150);
        
        // Should not affect other instance
        assertThat(properties2.getRateLimiting().getRequestsPerMinute()).isEqualTo(100);
    }

    @Test
    void shouldHandleNullValues() {
        SecurityProperties properties = new SecurityProperties();
        
        // Should not throw when setting null (though not recommended)
        properties.setAdditionalPublicUrls(null);
        
        // Getter should handle null gracefully
        assertThat(properties.getAdditionalPublicUrls()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        SecurityProperties properties = new SecurityProperties();
        
        properties.setEnableMethodSecurity(false);
        properties.setEnableCsrfProtection(true);
        properties.setAdditionalPublicUrls(List.of("/api/health"));
        
        SecurityProperties.RateLimiting rateLimiting = new SecurityProperties.RateLimiting();
        rateLimiting.setEnabled(true);
        rateLimiting.setRequestsPerMinute(200);
        rateLimiting.setBurstCapacity(400);
        properties.setRateLimiting(rateLimiting);
        
        assertThat(properties.isEnableMethodSecurity()).isFalse();
        assertThat(properties.isEnableCsrfProtection()).isTrue();
        assertThat(properties.getAdditionalPublicUrls()).containsExactly("/api/health");
        assertThat(properties.getRateLimiting().getRequestsPerMinute()).isEqualTo(200);
    }
}