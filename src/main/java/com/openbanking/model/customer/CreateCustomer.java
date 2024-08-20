package com.openbanking.model.customer;

import com.openbanking.enums.CustomerStatus;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import com.openbanking.validator.ValidRepresentativeEmail;
import com.openbanking.validator.ValidRepresentativePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateCustomer {
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String taxNo;
    private String address;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    private String representative;
    @ValidRepresentativeEmail
    private String representativeEmail;
    @ValidRepresentativePhone
    private String representativePhone;
    @NotNull
    private CustomerStatus status;
    private Boolean isParent;
    private Long parentId;
    List<CreateBankAccount> bankAccountList;
}
