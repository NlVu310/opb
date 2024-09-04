package com.openbanking.model.transaction_manage;

import lombok.Data;

@Data
public class DebtClearance {
    private String transId;
    private String transDate;
    private String customerId;
    private String billId;
    private String serviceId;
    private Integer amount;
    private String checksum;
}
