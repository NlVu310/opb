package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.AccountStatus;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import lombok.Data;

import java.util.List;

@Data
public class UpdateAccount extends BaseDTO {
    private String name;
    private String username;
    private Long customerId;
    private Long accountTypeId;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    private AccountStatus status;
    private String note;
    private List<Long> customerConcerned;
    private List<Long> partnerConcerned;
}
