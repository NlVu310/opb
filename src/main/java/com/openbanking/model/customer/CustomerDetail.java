package com.openbanking.model.customer;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.bank_account.BankAccount;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDetail extends BaseDTO {
    private String name;
    private String taxNo;
    private String address;
    private String email;
    private String phone;
    private String representative;
    private String representativeEmail;
    private String representativePhone;
    private String status;
    private Boolean isParent;
    private Long parentId;
    private String code;
    private List<BankAccount> bankAccounts;
}
