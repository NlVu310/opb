package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AccountTypeInfo extends BaseDTO {
    private String name;
    private String note;
    private String createdByName;
    private OffsetDateTime createdAt;
}
