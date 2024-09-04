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
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    private String newPassword;

    @NotNull
    private String reEnterNewPassword;

}
