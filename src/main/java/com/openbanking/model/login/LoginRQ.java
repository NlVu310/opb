package com.openbanking.model.login;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRQ {
    @NotNull
    private String username;
    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
