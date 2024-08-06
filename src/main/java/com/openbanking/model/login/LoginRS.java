package com.openbanking.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRS {
    private String token;
    private Long id;
    private String username;
    private String name;
}
