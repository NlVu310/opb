package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountEditHistoryRepository extends BaseRepository<BankAccountEditHistoryEntity, Long> {
    void deleteByBankAccountIdIn(List<Long> bankAccountIds);
    @Query(value = "select b from BankAccountEntity a join BankAccountEditHistoryEntity b on b.bankAccountId = a.id and b.bankAccountId = :id")
    List<BankAccountEditHistoryEntity> getListBankAccountEditHistoryByBankAccountId(Long id);
}
