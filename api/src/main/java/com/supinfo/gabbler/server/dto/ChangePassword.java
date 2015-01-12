package com.supinfo.gabbler.server.dto;

public class ChangePassword {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public ChangePassword setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangePassword setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

}
