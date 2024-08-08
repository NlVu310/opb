package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity, Long> , JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {
    Optional<AccountEntity> findByUsername(String username);

    List<AccountEntity> findByAccountTypeIdAndDeletedAtNull(Long accountTypeId);

}
