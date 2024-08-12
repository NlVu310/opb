package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AccountTypeRepository extends BaseRepository<AccountTypeEntity, Long> {
    @Query(value = "select at from AccountEntity a join AccountTypeEntity at on a.accountTypeId = at.id and a.id = :id")
    Page<AccountTypeEntity> getListAccountTypeByAccountId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT at FROM AccountTypeEntity at " +
            "JOIN AccountEntity a ON a.accountTypeId = at.id " +
            "WHERE a.id = :accountId " +
            "AND (:term IS NULL OR " +
            "(LOWER(at.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(at.note) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR at.createdAt = :termDate))")
    Page<AccountTypeEntity> searchAccountTypes(@Param("accountId") Long accountId,
                                               @Param("term") String term,
                                               @Param("termDate") OffsetDateTime termDate,
                                               Pageable pageable);


}

