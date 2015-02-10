package com.supinfo.gabbler.server.security;

import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.service.TokenService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class RestAuthenticationFilter extends GenericFilterBean {

    @Resource
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Resource
    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        UserDetails userDetails = loadUserDetails((HttpServletRequest) request);
        SecurityContext contextBeforeChainExecution = createSecurityContext(userDetails);

        try {
            SecurityContextHolder.setContext(contextBeforeChainExecution);
            if (contextBeforeChainExecution.getAuthentication() != null && contextBeforeChainExecution.getAuthentication().isAuthenticated()) {
                //Update last used

                String tokenStr = AuthHeaderUtil.getToken(((HttpServletRequest) request));
                if(tokenStr != null){
                    Token bddToken = tokenService.findOne(tokenStr);
                    if(bddToken != null){
                        bddToken.setLastUsed(new Date(System.currentTimeMillis()));
                        tokenService.update(bddToken);
                    }
                }
            }

            if(response instanceof HttpServletResponse){
                HttpServletResponse  httpResponse = (HttpServletResponse)response;
                httpResponse.setHeader("Access-Control-Allow-Origin", "*");
                httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                httpResponse.setHeader("Access-Control-Max-Age", "3600");
                httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Authorization, X-Requested-With");
                response = httpResponse;
            }

            filterChain.doFilter(request, response);
        }
        finally {
            // Clear the context and free the thread local
            SecurityContextHolder.clearContext();
        }
    }

    private SecurityContext createSecurityContext(UserDetails userDetails) {
        if (userDetails != null) {
            SecurityContextImpl securityContext = new SecurityContextImpl();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            securityContext.setAuthentication(authentication);
            return securityContext;
        }
        return SecurityContextHolder.createEmptyContext();
    }

    private UserDetails loadUserDetails(HttpServletRequest request) {
        String token = AuthHeaderUtil.getToken(request);
        if(token == null) return null;

        //Récupération du user associé au token si existant
        Token bddTokenInfo = tokenService.findOne(token);
        String userName = null;
        if(bddTokenInfo != null){
            userName = bddTokenInfo.getUsername();
        }

        return userName != null
                ? userDetailsService.loadUserByUsername(userName)
                : null;
    }
}