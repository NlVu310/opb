package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateAccountType extends BaseDTO {
    private String name;
    private String note;
    private List<Long> permissionIds;
}
