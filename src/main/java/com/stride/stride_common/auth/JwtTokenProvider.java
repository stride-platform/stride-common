package com.stride.stride_common.auth;

import com.stride.stride_common.exceptions.AuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * JWT token provider for creating and validating authentication tokens
 */
@Slf4j
@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long accessTokenValidityInMinutes;
    private final long refreshTokenValidityInDays;
    private final String issuer;
    
    public JwtTokenProvider(
            @Value("${stride.jwt.secret:defaultSecretKeyThatIsAtLeast256BitsLongForHS256Algorithm}") String secret,
            @Value("${stride.jwt.access-token-validity-minutes:60}") long accessTokenValidityInMinutes,
            @Value("${stride.jwt.refresh-token-validity-days:30}") long refreshTokenValidityInDays,
            @Value("${stride.jwt.issuer:stride-platform}") String issuer) {
        
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityInMinutes = accessTokenValidityInMinutes;
        this.refreshTokenValidityInDays = refreshTokenValidityInDays;
        this.issuer = issuer;
    }
    
    /**
     * Generate access token for authenticated user
     */
    public String generateAccessToken(String userId, String organizationId, List<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plus(accessTokenValidityInMinutes, ChronoUnit.MINUTES);
        
        return Jwts.builder()
            .subject(userId)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("organizationId", organizationId)
            .claim("roles", roles)
            .claim("type", "access")
            .signWith(secretKey)
            .compact();
    }
    
    /**
     * Generate refresh token for token renewal
     */
    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        Instant expiration = now.plus(refreshTokenValidityInDays, ChronoUnit.DAYS);
        
        return Jwts.builder()
            .subject(userId)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("type", "refresh")
            .signWith(secretKey)
            .compact();
    }
    
    /**
     * Validate token and return claims
     */
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            
            log.warn("JWT token expired: {}", e.getMessage());
            throw AuthenticationException.tokenExpired();
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw AuthenticationException.invalidToken();
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw AuthenticationException.invalidToken();
        } catch (SecurityException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw AuthenticationException.invalidToken();
        }
    }
    
    /**
     * Extract user ID from token
     */
    public String getUserId(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }
    
    /**
     * Extract organization ID from token
     */
    public String getOrganizationId(String token) {
        Claims claims = validateToken(token);
        return claims.get("organizationId", String.class);
    }
    
    /**
     * Extract roles from token
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Claims claims = validateToken(token);
        return claims.get("roles", List.class);
    }
    
    /**
     * Check if token is access token
     */
    public boolean isAccessToken(String token) {
        Claims claims = validateToken(token);
        return "access".equals(claims.get("type", String.class));
    }
    
    /**
     * Check if token is refresh token
     */
    public boolean isRefreshToken(String token) {
        Claims claims = validateToken(token);
        return "refresh".equals(claims.get("type", String.class));
    }
    
    /**
     * Get token expiration time
     */
    public Instant getExpirationTime(String token) {
        Claims claims = validateToken(token);
        return claims.getExpiration().toInstant();
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Instant expiration = getExpirationTime(token);
            return expiration.isBefore(Instant.now());
        } catch (AuthenticationException e) {
            return true; // Consider invalid tokens as expired
        }
    }
}