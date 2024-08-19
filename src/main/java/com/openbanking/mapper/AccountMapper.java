package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountEntity;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.AccountInfo;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> {

    @Mapping(source = "accountName", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "accountTypeName", target = "accountType.name")
    @Mapping(source = "customerName", target = "customer.name")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapToOffsetDateTime")
    Account toDTOFromDetail(AccountInfo accountInfo);

    @Named("mapToOffsetDateTime")
    default OffsetDateTime mapToOffsetDateTime(LocalDateTime createdAt) {
        return createdAt != null ? createdAt.atOffset(ZoneOffset.UTC) : null;
    }
}



