package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.permission.PermissionRS;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AccountTypeInfo extends BaseDTO {
    private String name;
    private String note;
    private PermissionRS permissions;
    private String createdByName;
    private OffsetDateTime createdAt;

    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return createdAt.format(formatter);
        }
        return null;
    }
}
