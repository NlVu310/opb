package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.PartnerStatus;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import lombok.Data;

@Data
public class UpdatePartner extends BaseDTO {
    private String name;
    private String address;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    private String code;

    private PartnerStatus status;
}
