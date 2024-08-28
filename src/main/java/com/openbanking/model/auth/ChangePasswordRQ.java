package com.openbanking.model.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRQ {
    @NotNull
    private Long id;

    @NotNull
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    private String newPassword;

    @NotNull
    private String reEnterNewPassword;

}
