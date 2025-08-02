package com.stride.stride_common.security;


import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration properties for Stride services
 */
@Configuration
@ConfigurationProperties(prefix = "stride.security")
public class SecurityProperties {
    
    private boolean enableMethodSecurity = true;
    private boolean enableCsrfProtection = false;
    private List<String> additionalPublicUrls = List.of();
    private RateLimiting rateLimiting = new RateLimiting();
    
    // Getters and Setters
    public boolean isEnableMethodSecurity() {
        return enableMethodSecurity;
    }
    
    public void setEnableMethodSecurity(boolean enableMethodSecurity) {
        this.enableMethodSecurity = enableMethodSecurity;
    }
    
    public boolean isEnableCsrfProtection() {
        return enableCsrfProtection;
    }
    
    public void setEnableCsrfProtection(boolean enableCsrfProtection) {
        this.enableCsrfProtection = enableCsrfProtection;
    }
    
    public List<String> getAdditionalPublicUrls() {
        return additionalPublicUrls;
    }
    
    public void setAdditionalPublicUrls(List<String> additionalPublicUrls) {
        this.additionalPublicUrls = additionalPublicUrls;
    }
    
    public RateLimiting getRateLimiting() {
        return rateLimiting;
    }
    
    public void setRateLimiting(RateLimiting rateLimiting) {
        this.rateLimiting = rateLimiting;
    }
    
    /**
     * Rate limiting configuration
     */
    public static class RateLimiting {
        private boolean enabled = true;
        private int requestsPerMinute = 100;
        private int burstCapacity = 200;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public int getRequestsPerMinute() {
            return requestsPerMinute;
        }
        
        public void setRequestsPerMinute(int requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
        }
        
        public int getBurstCapacity() {
            return burstCapacity;
        }
        
        public void setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
        }
    }
}