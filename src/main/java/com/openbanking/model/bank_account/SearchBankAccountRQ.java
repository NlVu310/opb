package com.openbanking.model.bank_account;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

@Data
public class SearchBankAccountRQ {
    private Long customerId;
    private String status;
    private String partnerName;
}
