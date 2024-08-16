package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.enums.ReconciliationFrequencyUnit;
import lombok.Data;
import java.time.LocalTime;

@Data
public class CreateReconciliationRQ {
    private Long sourceId;
    private String sourceCode;
    private String reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private ReconciliationFrequencyUnit reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
}
