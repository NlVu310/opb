package com.openbanking.model.customer;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.CustomerStatus;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import lombok.Data;

import java.util.List;
@Data
public class UpdateCustomer extends BaseDTO {
    private String name;
    private String taxNo;
    private String address;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    private String representative;
    @ValidEmail
    private String representativeEmail;
    @ValidPhone
    private String representativePhone;
    private CustomerStatus status;
    private Boolean isParent;
    private Long parentId;
    private List<UpdateBankAccount> listUpdateBankAccounts;
}
