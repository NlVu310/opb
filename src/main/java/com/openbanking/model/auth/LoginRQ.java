package com.openbanking.model.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRQ {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
