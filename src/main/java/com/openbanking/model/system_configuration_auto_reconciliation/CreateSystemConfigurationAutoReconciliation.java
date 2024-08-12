package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class CreateSystemConfigurationAutoReconciliation extends BaseDTO {
    private Long partnerId;

    private Timestamp reconciliationTime;

    private Long reconciliationFrequencyId;

    private Long accountId;

    private Long sourceId;
}


