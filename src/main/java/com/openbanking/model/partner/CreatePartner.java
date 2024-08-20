package com.openbanking.model.partner;

import com.openbanking.enums.PartnerStatus;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreatePartner  {
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String address;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    @NotNull
    private PartnerStatus status;
}
