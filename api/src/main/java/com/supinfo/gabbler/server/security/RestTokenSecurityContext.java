package com.supinfo.gabbler.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

public class RestTokenSecurityContext implements SecurityContext {
    private static final long serialVersionUID = 1L;

    private Authentication authentication;

    public RestTokenSecurityContext(UserDetails userDetails) {
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        throw new UnsupportedOperationException("Should not be called by the code path");
    }

}