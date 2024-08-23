package com.openbanking.model.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "password")
public class PasswordProperties {
    private String defaultPassword;
}

