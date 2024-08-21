package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.AccountStatus;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.customer.Customer;
import lombok.Data;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class Account extends BaseDTO {
    private String name;
    private String username;
    private String email;
    private String phone;
    private AccountStatus status;
    private String note;
    private Customer customer;
    private AccountType accountType;
    private OffsetDateTime createdAt;
    private String createdByName;
    private List<Long> customerConcerned;
    private List<Long> partnerConcerned;


    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return createdAt.format(formatter);
        }
        return null;
    }
}
