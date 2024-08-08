package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.permission.Permission;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Data
public class AccountTypeDetail extends BaseDTO {
    private String name;
    private String note;
    private List<Permission> permissions;
}
