package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.transaction_manage_reconciliation_history.CreateTransactionManageReconciliationHistory;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import com.openbanking.model.transaction_manage_reconciliation_history.UpdateTransactionManageReconciliationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface TransactionManageReconciliationHistoryMapper extends BaseMapper<TransactionManageReconciliationHistoryEntity,
        TransactionManageReconciliationHistory, CreateTransactionManageReconciliationHistory, UpdateTransactionManageReconciliationHistory> {
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "reconciliationDate", target = "reconciliationDate", qualifiedByName = "offsetDateTimeToString")
    TransactionManageReconciliationHistory toDTO(TransactionManageReconciliationHistoryEntity transactionManageReconciliationHistoryEntity);

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "reconciliationDate", target = "reconciliationDate", qualifiedByName = "stringToOffsetDateTime")
    TransactionManageReconciliationHistoryEntity toEntity(TransactionManageReconciliationHistory transactionManageReconciliationHistory);

    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String date) {
        return date == null ? null : OffsetDateTime.parse(date + "T00:00:00+00:00");
    }

    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}


