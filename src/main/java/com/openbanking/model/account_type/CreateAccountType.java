package com.openbanking.model.account_type;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAccountType {
    @NotNull
    private String name;
    private String note;
    private List<Long> permissionIds;
}
