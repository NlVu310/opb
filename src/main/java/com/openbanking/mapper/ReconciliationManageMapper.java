package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.ReconciliationManageEntity;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.model.reconciliation_manage.CreateReconciliationManage;
import com.openbanking.model.reconciliation_manage.ReconciliationManage;
import com.openbanking.model.reconciliation_manage.ReconciliationManageDetail;
import com.openbanking.model.reconciliation_manage.UpdateReconciliationManage;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.model.transaction_manage.TransactionManageDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface ReconciliationManageMapper extends BaseMapper<ReconciliationManageEntity, ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage> {

}

