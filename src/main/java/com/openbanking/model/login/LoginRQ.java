package com.openbanking.model.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRQ {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
