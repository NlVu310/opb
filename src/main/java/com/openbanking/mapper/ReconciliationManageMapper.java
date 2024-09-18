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
    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "reconciliationDate", target = "reconciliationDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(target = "amount", source = "reconciliationManageEntity", qualifiedByName = "adjustAmount")
    ReconciliationManage toDTO(ReconciliationManageEntity reconciliationManageEntity);

    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "reconciliationDate", target = "reconciliationDate", qualifiedByName = "stringToOffsetDateTime")
    ReconciliationManageEntity toEntity(ReconciliationManage reconciliationManage);

    @Mapping(target = "amount", source = "reconciliationManageEntity", qualifiedByName = "adjustAmount")
    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "reconciliationDate", target = "reconciliationDate", qualifiedByName = "offsetDateTimeToString")
    ReconciliationManageDetail getDetail(ReconciliationManageEntity reconciliationManageEntity);
    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String date) {
        return date == null ? null : OffsetDateTime.parse(date + "T00:00:00+00:00");
    }

    @Named("adjustAmount")
    static String adjustAmount(ReconciliationManageEntity reconciliationManageEntity) {
        String amount = reconciliationManageEntity.getAmount();
        String dorc = reconciliationManageEntity.getDorc();
        if (amount == null) {
            return null;
        }
        switch (dorc) {
            case "D":
                return "-" + amount;
            case "C":
                return "+" + amount;
            default:
                return amount;
        }
    }
}

