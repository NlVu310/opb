package com.openbanking.model.account_type;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Data
public class AccountTypeDetail {
    private String name;
    private String note;
    private List<Long> permissionIds;
}
