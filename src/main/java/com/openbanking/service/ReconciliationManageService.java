package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.model.reconciliation_manage.*;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.transaction_manage.Iconnect;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReconciliationManageService extends BaseService<ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage, Long> {
    void handleIconnectReconciliations(List<ReconciliationIconnect> iconnects);
    public void performReconciliation();
}
