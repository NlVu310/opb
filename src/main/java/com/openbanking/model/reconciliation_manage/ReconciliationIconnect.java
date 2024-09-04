package com.openbanking.model.reconciliation_manage;

import lombok.Data;

@Data
public class ReconciliationIconnect {
    private String transDate;
    private String transTime;
    private String accountNo;
    private String dorc;
    private String currency;
    private String amount;
    private String remark;
    private String refNo;
    private String frBankCode;
    private String frAccName;
    private String frAccNo;
    private String frBankName;
    private String seq;
    private String endBal;
    private String channelRef;
    private String channelID;
    private String icnRef;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String from;
}
