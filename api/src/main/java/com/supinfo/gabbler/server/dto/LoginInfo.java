package com.supinfo.gabbler.server.dto;

public class LoginInfo {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginInfo setPassword(String password) {
        this.password = password;
        return this;
    }
}
