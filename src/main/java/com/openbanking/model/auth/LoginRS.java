package com.openbanking.model.auth;

import com.openbanking.model.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginRS {
    private String token;
    private String refreshToken;
    private Long id;
    private String username;
    private String name;
    private Boolean isChangedPassword;
    private List<Permission> permissions;
}
