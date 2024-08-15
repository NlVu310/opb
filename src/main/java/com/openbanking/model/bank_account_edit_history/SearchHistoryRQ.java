package com.openbanking.model.bank_account_edit_history;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

@Data
public class SearchHistoryRQ extends SearchCriteria {
    private String status;
    private String partnerName;
}
