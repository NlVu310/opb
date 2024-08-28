package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.enums.ReconciliationFrequencyUnit;

import java.time.LocalTime;

public interface SystemConfigurationAutoReconciliationProjection {
    Long getId();

    Long getSourceId();

    String getSourceCode();

    LocalTime getReconciliationTime();

    Integer getReconciliationFrequencyNumber();

    ReconciliationFrequencyUnit getReconciliationFrequencyUnit();

    Integer getRetryTimeNumber();

    Integer getRetryFrequencyNumber();
}
