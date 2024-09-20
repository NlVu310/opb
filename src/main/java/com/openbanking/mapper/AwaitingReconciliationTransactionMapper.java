package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AwaitingReconciliationTransactionEntity;
import com.openbanking.model.awaiting_reconciliation_transactions.AwaitingReconciliationTransaction;
import com.openbanking.model.awaiting_reconciliation_transactions.CreateAwaitingReconciliationTransaction;
import com.openbanking.model.awaiting_reconciliation_transactions.UpdateAwaitingReconciliationTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface AwaitingReconciliationTransactionMapper extends BaseMapper<AwaitingReconciliationTransactionEntity, AwaitingReconciliationTransaction, CreateAwaitingReconciliationTransaction, UpdateAwaitingReconciliationTransaction> {
    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(target = "amount", source = "awaitingReconciliationTransactionEntity", qualifiedByName = "adjustAmount")
    AwaitingReconciliationTransaction toDTO(AwaitingReconciliationTransactionEntity awaitingReconciliationTransactionEntity);

    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @Named("adjustAmount")
    static String adjustAmount(AwaitingReconciliationTransactionEntity awaitingReconciliationTransactionEntity) {
        String amount = awaitingReconciliationTransactionEntity.getAmount();
        String dorc = awaitingReconciliationTransactionEntity.getDorc();
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

    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "stringToOffsetDateTime")
    AwaitingReconciliationTransactionEntity toEntity(AwaitingReconciliationTransaction awaitingReconciliationTransaction);
    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String date) {
        return date == null ? null : OffsetDateTime.parse(date + "T00:00:00+00:00");
    }


}
