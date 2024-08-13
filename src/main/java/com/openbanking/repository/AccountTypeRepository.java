package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface AccountTypeRepository extends BaseRepository<AccountTypeEntity, Long> {
    @Query(value = "select at from AccountTypeEntity at")
    Page<AccountTypeEntity> getListAccountType(Pageable pageable);

    @Query("SELECT at FROM AccountTypeEntity at")
    Page<AccountTypeEntity> findAllWithPagination(Pageable pageable);

    @Query("SELECT at FROM AccountTypeEntity at " +
            "JOIN AccountEntity a ON a.accountTypeId = at.id " +
            "AND (:term IS NULL OR " +
            "(LOWER(at.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(at.note) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :term, '%')))) " +
            "AND (cast(:date as date) IS NULL OR at.createdAt = :date)")
    Page<AccountTypeEntity> searchAccountTypes(@Param("term") String term,
                                               @Param("date") OffsetDateTime date,
                                               Pageable pageable);


}

