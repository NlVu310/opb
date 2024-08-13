package com.openbanking.model.system_configuration_transaction_content;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class SystemConfigurationTransactionContent extends BaseDTO {
    private Long customerId;
    private Long source;
    private Long refNoLength;
    private Long refNoStart;
    private Long refNoEnd;
    private Long accountId;
    private String customerName;
}
