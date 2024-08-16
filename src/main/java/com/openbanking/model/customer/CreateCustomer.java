package com.openbanking.model.customer;

import com.openbanking.enums.CustomerStatus;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import lombok.Data;

import java.util.List;

@Data
public class CreateCustomer {
    private String name;
    private String taxNo;
    private String address;
    private String email;
    private String phone;
    private String representative;
    private String representativeEmail;
    private String representativePhone;
    private CustomerStatus status;
    private Boolean isParent;
    private Long parentId;
    List<CreateBankAccount> bankAccountList;
}
