package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends BaseRepository<AccountTypeEntity, Long> {
}
