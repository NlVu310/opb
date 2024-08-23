package com.openbanking.model.auth;

import lombok.Data;

@Data
public class RegisterRQ {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
}
