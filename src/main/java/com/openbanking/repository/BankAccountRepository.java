package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends BaseRepository<BankAccountEntity, Long> {
    @Query(value = "select b from CustomerEntity c join BankAccountEntity b on b.customerId = c.id and b.customerId = :id")
    List<BankAccountEntity> getListBankAccountByCustomerId(Long id);
}
