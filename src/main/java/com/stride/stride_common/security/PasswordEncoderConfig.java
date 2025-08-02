package com.stride.stride_common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password encoding configuration for all Stride services
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * BCrypt password encoder with strength 12 for enhanced security
     * Higher strength = more secure but slower hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}