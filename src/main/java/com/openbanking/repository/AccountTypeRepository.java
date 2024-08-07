package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypeRepository extends BaseRepository<AccountTypeEntity, Long> {
    @Query(value = "select at from AccountEntity a join AccountTypeEntity at on a.accountTypeId = at.id and a.id = :id")
    List<AccountTypeEntity> getListAccountTypeByAccountId(@Param("id") Long id);
}
