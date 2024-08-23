package com.openbanking.model.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRQ {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotNull
    private String reEnterNewPassword;

}
