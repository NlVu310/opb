package com.openbanking.model.bank_account;

import com.openbanking.comon.SearchCriteria;
import com.openbanking.enums.BankAccountStatus;
import lombok.Data;

@Data
public class SearchBankAccountRQ {
    private Long customerId;
    private BankAccountStatus status;
    private String partnerName;
}
