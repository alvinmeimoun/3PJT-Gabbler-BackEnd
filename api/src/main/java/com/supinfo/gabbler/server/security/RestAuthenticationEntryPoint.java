package com.supinfo.gabbler.server.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_JSON = "{\"message\":\"Authentication is required to access this resource.\", \"access-denied\":true,\"cause\":\"NOT AUTHENTICATED\"}";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print(UNAUTHORIZED_JSON);
        out.flush();
        out.close();
    }
}