package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.reconciliation_manage.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReconciliationManageService extends BaseService<ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage, Long> {

}
