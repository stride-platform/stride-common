package com.stride.stride_common.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CORS configuration for microservices communication and frontend access
 */
@Configuration
public class CorsConfig {
    
    @Value("${app.cors.allowed-origins:}")
    private List<String> additionalAllowedOrigins = new ArrayList<>();
    
    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials = true;
    
    @Value("${app.cors.max-age:3600}")
    private long maxAge = 3600;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Combine default and additional allowed origins
        List<String> allAllowedOrigins = new ArrayList<>(Arrays.asList(SecurityConstants.ALLOWED_ORIGINS));
        if (additionalAllowedOrigins != null && !additionalAllowedOrigins.isEmpty()) {
            allAllowedOrigins.addAll(additionalAllowedOrigins);
        }
        
        configuration.setAllowedOriginPatterns(allAllowedOrigins);
        configuration.setAllowedMethods(Arrays.asList(SecurityConstants.ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(SecurityConstants.ALLOWED_HEADERS));
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);
        
        // Allow common response headers to be exposed
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-Total-Count",
            "X-Page-Count",
            "X-Current-Page",
            SecurityConstants.ORGANIZATION_HEADER,
            SecurityConstants.USER_CONTEXT_HEADER
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}