package com.openbanking.model.system_configuration_auto_reconciliation;

import lombok.Data;
import java.time.LocalTime;

@Data
public class CreateReconciliationRQ {
    private Long sourceId;
    private String sourceCode;
    private LocalTime reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private String reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
}
