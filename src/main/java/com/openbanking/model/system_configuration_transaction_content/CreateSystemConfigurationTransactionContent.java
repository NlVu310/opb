package com.openbanking.model.system_configuration_transaction_content;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class CreateSystemConfigurationTransactionContent{
    @NotNull
    private Long customerId;
    @NotNull
    private String sourceStart;
    private Long sourceLengthEnd;
    private String sourceIndexEnd;
    private String sourceRegex;
    private String refNoStart;
    private Long refNoLengthEnd;
    private String refNoIndexEnd;
    private String refNoRegex;
}

