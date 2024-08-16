package com.openbanking.model.system_configuration_transaction_content;
import lombok.Data;

@Data
public class CreateSystemConfigurationTransactionContent{
    private Long customerId;
    private Long source;
    private Long refNoLength;
    private Long refNoStart;
    private Long refNoEnd;
}
