package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.*;
import com.openbanking.model.reconciliation_manage.*;
import com.openbanking.service.ReconciliationManageService;
import org.springframework.stereotype.Service;

@Service
public class ReconciliationManageServiceImpl extends BaseServiceImpl<ReconciliationManageEntity, ReconciliationManage,CreateReconciliationManage, UpdateReconciliationManage, Long> implements ReconciliationManageService {

    public ReconciliationManageServiceImpl(BaseRepository<ReconciliationManageEntity, Long> repository, BaseMapper<ReconciliationManageEntity, ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage> mapper) {
        super(repository, mapper);
    }

}