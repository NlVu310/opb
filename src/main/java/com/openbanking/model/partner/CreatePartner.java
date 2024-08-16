package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.PartnerStatus;
import com.openbanking.validator.ValidPhone;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreatePartner  {
    @NotNull
    private String name;
    private String address;
    @ValidPhone
    private String email;
    @ValidPhone
    private String phone;
    @NotNull
    private PartnerStatus status;
}
