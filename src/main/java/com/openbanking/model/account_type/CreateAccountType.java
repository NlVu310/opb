package com.openbanking.model.account_type;

import lombok.Data;

import java.util.List;

@Data
public class CreateAccountType {
    private String name;
    private String note;
    private List<Long> permissionIds;
}
