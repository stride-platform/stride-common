package com.stride.stride_common.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * JWT Authentication Token for Spring Security
 */
@Slf4j
public class JwtAuthenticationToken implements Authentication {
    
    private final String token;
    private final UserPrincipal principal;
    private final List<GrantedAuthority> authorities;
    private boolean authenticated;
    
    public JwtAuthenticationToken(String token) {
        this.token = token;
        this.principal = null;
        this.authorities = List.of();
        this.authenticated = false;
    }
    
    public JwtAuthenticationToken(String token, UserPrincipal principal, 
                                 List<GrantedAuthority> authorities) {
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public Object getDetails() {
        return null;
    }
}