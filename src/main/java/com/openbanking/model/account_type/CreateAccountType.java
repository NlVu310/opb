package com.openbanking.model.account_type;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAccountType {
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String note;
    private List<Long> permissionIds;
}
