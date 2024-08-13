package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.customer.Customer;
import lombok.Data;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Account extends BaseDTO {
    private String name;
    private String username;
    private String email;
    private String phone;
    private String status;
    private String note;
    private Customer customer;
    private AccountType accountType;
    private OffsetDateTime createdAt;
    private String createdByName;

    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return createdAt.format(formatter);
        }
        return null;
    }
}
