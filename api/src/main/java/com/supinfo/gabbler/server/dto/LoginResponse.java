package com.supinfo.gabbler.server.dto;

public class LoginResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }
}
