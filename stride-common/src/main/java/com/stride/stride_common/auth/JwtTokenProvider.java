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
import java.util.Map;

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
            @Value("${stride.jwt.secret}") String secret,
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

/**
 * JWT Authentication Token for Spring Security
 */
@Slf4j
public class JwtAuthenticationToken implements org.springframework.security.core.Authentication {
    
    private final String token;
    private final UserPrincipal principal;
    private final List<org.springframework.security.core.GrantedAuthority> authorities;
    private boolean authenticated;
    
    public JwtAuthenticationToken(String token) {
        this.token = token;
        this.principal = null;
        this.authorities = List.of();
        this.authenticated = false;
    }
    
    public JwtAuthenticationToken(String token, UserPrincipal principal, 
                                 List<org.springframework.security.core.GrantedAuthority> authorities) {
        this.token = token;
        this.principal = principal;
        this.authorities = authorities;
        this.authenticated = true;
    }
    
    @Override
    public Object getCredentials() {
        return token;
    }
    
    @Override
    public Object getPrincipal() {
        return principal;
    }
    
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor");
        }
        this.authenticated = false;
    }
    
    @Override
    public String getName() {
        return principal != null ? principal.getUsername() : null;
    }
    
    @Override
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public Object getDetails() {
        return null;
    }
}

/**
 * User principal for JWT authentication
 */
public record UserPrincipal(
    String userId,
    String username,
    String organizationId,
    List<String> roles,
    boolean enabled
) implements org.springframework.security.core.userdetails.UserDetails {
    
    @Override
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
            .toList();
    }
    
    @Override
    public String getPassword() {
        // JWT tokens don't store passwords
        return null;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }
    
    @Override
    public boolean isAccountNonLocked() {