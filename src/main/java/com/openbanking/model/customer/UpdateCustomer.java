package com.openbanking.model.customer;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.CustomerStatus;
import com.openbanking.model.bank_account.UpdateBankAccount;
import lombok.Data;

import java.util.List;
@Data
public class UpdateCustomer extends BaseDTO {
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
    private List<UpdateBankAccount> listUpdateBankAccounts;
}
