package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.ListPartnerInfo;
import com.openbanking.model.bank_account.UpdateBankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface BankAccountMapper extends BaseMapper<BankAccountEntity, BankAccount, CreateBankAccount, UpdateBankAccount> {
    BankAccountEntity getEntity(UpdateBankAccount updateBankAccount);
    @Mapping(source = "fromDate", target = "fromDate", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "toDate", target = "toDate", qualifiedByName = "offsetDateTimeToString")
    BankAccount toDTO(BankAccountEntity bankAccountEntity);

    @Named("offsetDateTimeToString")
    static String offsetDateTimeToString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Mapping(source = "fromDate", target = "fromDate", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "toDate", target = "toDate", qualifiedByName = "stringToOffsetDateTime")
    BankAccountEntity toEntity(BankAccount updateBankAccount);

    @Named("stringToOffsetDateTime")
    static OffsetDateTime stringToOffsetDateTime(String dateString) {
        return dateString != null ? OffsetDateTime.parse(dateString) : null;
    }
}
