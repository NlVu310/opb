package com.openbanking.model.bank_account_edit_history;

import com.openbanking.comon.BaseDTO;
import lombok.Data;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class BankAccountEditHistory extends BaseDTO {
    private Long bankAccountId;
    private String oldFromDate;
    private String oldToDate;
    private String newFromDate;
    private String newToDate;
    private String createdAt;
}
