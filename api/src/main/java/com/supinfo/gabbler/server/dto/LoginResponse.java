package com.supinfo.gabbler.server.dto;

public class LoginResponse {

    private String token;

    private Long userID;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getUserID() {
        return userID;
    }

    public LoginResponse setUserID(Long userID) {
        this.userID = userID;
        return this;
    }
}
