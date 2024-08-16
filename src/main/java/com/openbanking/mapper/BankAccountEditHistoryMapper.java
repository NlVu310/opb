package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface BankAccountEditHistoryMapper extends BaseMapper<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory> {
    @Mapping(source = "oldFromDate", target = "oldFromDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "oldToDate", target = "oldToDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "newFromDate", target = "newFromDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "newToDate", target = "newToDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "offsetDateTimeToString")
    BankAccountEditHistory toDTO(BankAccountEditHistoryEntity bankAccountEditHistoryEntity);

    @Mapping(source = "oldFromDate", target = "oldFromDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "oldToDate", target = "oldToDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "newFromDate", target = "newFromDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "newToDate", target = "newToDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToOffsetDateTime")
    BankAccountEditHistoryEntity toEntity(BankAccountEditHistory bankAccountEditHistory);

    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String date) {
        return date == null ? null : OffsetDateTime.parse(date + "T00:00:00+00:00");
    }

    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }


}
