package com.openbanking.model.bank_account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ListPartnerInfo {
    private Long partnerId;
    private String partnerName;

}
