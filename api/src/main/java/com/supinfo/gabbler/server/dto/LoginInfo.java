package com.supinfo.gabbler.server.dto;

import javax.validation.constraints.NotNull;

public class LoginInfo {

    private @NotNull String username;
    private @NotNull String password;

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
