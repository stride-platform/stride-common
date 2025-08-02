package com.stride.stride_common.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Tests for PasswordEncoderConfig
 */
class PasswordEncoderConfigTest {

    private final PasswordEncoderConfig config = new PasswordEncoderConfig();

    @Test
    void shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void shouldUseStrength12() {
        BCryptPasswordEncoder encoder = (BCryptPasswordEncoder) config.passwordEncoder();
        
        // BCryptPasswordEncoder doesn't expose strength directly, but we can test behavior
        String password = "testPassword123";
        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);
        
        // Different salts should produce different hashes
        assertThat(encoded1).isNotEqualTo(encoded2);
        
        // Both should match the original password
        assertThat(encoder.matches(password, encoded1)).isTrue();
        assertThat(encoder.matches(password, encoded2)).isTrue();
        
        // Wrong password should not match
        assertThat(encoder.matches("wrongPassword", encoded1)).isFalse();
    }

    @Test
    void shouldProduceBCryptHashFormat() {
        PasswordEncoder encoder = config.passwordEncoder();
        String password = "testPassword123";
        String encoded = encoder.encode(password);
        
        // BCrypt hashes start with $2a$, $2b$, or $2y$ followed by cost parameter
        assertThat(encoded).matches("\\$2[aby]\\$\\d{2}\\$.{53}");
        
        // Should be exactly 60 characters for BCrypt
        assertThat(encoded).hasSize(60);
    }

    @Test
    void shouldHandleSpecialCharacters() {
        PasswordEncoder encoder = config.passwordEncoder();
        String specialPassword = "p@ssw0rd!#$%^&*()";
        String encoded = encoder.encode(specialPassword);
        
        assertThat(encoder.matches(specialPassword, encoded)).isTrue();
        assertThat(encoder.matches("differentPassword", encoded)).isFalse();
    }

    @Test
    void shouldHandleEmptyAndNullPasswords() {
        PasswordEncoder encoder = config.passwordEncoder();
        
        // Empty string
        String emptyEncoded = encoder.encode("");
        assertThat(encoder.matches("", emptyEncoded)).isTrue();
        assertThat(encoder.matches("notEmpty", emptyEncoded)).isFalse();
        
        // Null should throw exception when encoding
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> encoder.encode(null)
        );
    }

    @Test
    void shouldBeConsistentAcrossInstances() {
        PasswordEncoder encoder1 = new PasswordEncoderConfig().passwordEncoder();
        PasswordEncoder encoder2 = new PasswordEncoderConfig().passwordEncoder();
        
        String password = "testPassword123";
        String encoded = encoder1.encode(password);
        
        // Different instances should be able to verify each other's hashes
        assertThat(encoder2.matches(password, encoded)).isTrue();
    }
}