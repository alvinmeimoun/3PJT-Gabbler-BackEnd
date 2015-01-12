package com.supinfo.gabbler.server.dto;

import javax.validation.constraints.NotNull;

public class ChangePassword {

    private @NotNull String oldPassword;
    private @NotNull String newPassword;

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
