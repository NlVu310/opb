package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.UpdateSystemConfigurationAutoReconciliation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SystemConfigurationAutoReconciliationService extends BaseService<SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation, Long> {
    void create(CreateSystemConfigurationAutoReconciliation createSystemConfigurationAutoReconciliation);
    void deleteListById(List<Long> ids);
    SystemConfigurationAutoReconciliation getDetailById(Long id);
    List<SystemConfigurationAutoReconciliation> getListByPartnerId(Long id);
}
