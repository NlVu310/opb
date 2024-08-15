package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.ListPartnerInfo;
import com.openbanking.model.bank_account.UpdateBankAccount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface BankAccountMapper extends BaseMapper<BankAccountEntity, BankAccount, CreateBankAccount, UpdateBankAccount> {
    BankAccountEntity getEntity(UpdateBankAccount updateBankAccount);
}
