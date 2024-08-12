package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAccountType extends BaseDTO {
    @NotNull
    private String name;
    private String note;
    private List<Long> permissionIds;
}
