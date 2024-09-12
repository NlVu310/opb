package com.openbanking.model.system_configuration_transaction_content;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SystemConfigurationTransactionContent extends BaseDTO {
    private Long customerId;
    private String sourceStart;
    private Long sourceLengthEnd;
    private String sourceIndexEnd;
    private String sourceRegex;
    private String refNoStart;
    private Long refNoLengthEnd;
    private String refNoIndexEnd;
    private String refNoRegex;
    private String customerName;
}
