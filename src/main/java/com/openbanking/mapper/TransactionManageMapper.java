package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.model.transaction_manage.CreateTransactionManage;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.model.transaction_manage.TransactionManageDetail;
import com.openbanking.model.transaction_manage.UpdateTransactionManage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface TransactionManageMapper extends BaseMapper<TransactionManageEntity, TransactionManage, CreateTransactionManage, UpdateTransactionManage> {

    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(target = "amount", source = "transactionManageEntity", qualifiedByName = "adjustAmount")
    TransactionManage toDTO(TransactionManageEntity transactionManageEntity);

    @Mapping(target = "amount", source = "transactionManageEntity", qualifiedByName = "adjustAmount")
    @Mapping(source = "transactionDate", target = "transactionDate", qualifiedByName = "offsetDateTimeToString")
    TransactionManageDetail getDetail(TransactionManageEntity transactionManageEntity);
    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @Named("adjustAmount")
    static String adjustAmount(TransactionManageEntity transactionManageEntity) {
        String amount = transactionManageEntity.getAmount();
        String dorc = transactionManageEntity.getDorc();
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
    TransactionManageEntity toEntity(TransactionManage transactionManage);
    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String date) {
        return date == null ? null : OffsetDateTime.parse(date + "T00:00:00+00:00");
    }


}
