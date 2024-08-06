package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
}
