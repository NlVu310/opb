package com.openbanking.model.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRQ {
    @NotNull
    private String username;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
