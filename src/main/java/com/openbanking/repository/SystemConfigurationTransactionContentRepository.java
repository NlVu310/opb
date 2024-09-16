package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigurationTransactionContentRepository extends BaseRepository<SystemConfigurationTransactionContentEntity, Long> {
    @Query("SELECT stc.id AS id, stc.customerId AS customerId, " +
            "stc.sourceStart AS sourceStart, " +
            "stc.sourceLengthEnd AS sourceLengthEnd, " +
            "stc.sourceIndexEnd AS sourceIndexEnd, " +
            "stc.sourceRegex AS sourceRegex, " +
            "stc.refNoStart AS refNoStart, " +
            "stc.refNoLengthEnd AS refNoLengthEnd, " +
            "stc.refNoIndexEnd AS refNoIndexEnd, " +
            "stc.refNoRegex AS refNoRegex, " +
            "c.name AS customerName " +
            "FROM SystemConfigurationTransactionContentEntity stc " +
            "LEFT JOIN CustomerEntity c ON stc.customerId = c.id " +
            "WHERE (:term IS NULL OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.sourceStart) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(stc.sourceLengthEnd AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.sourceIndexEnd) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.sourceRegex) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.refNoStart) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(stc.refNoLengthEnd AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.refNoIndexEnd) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(stc.refNoRegex) LIKE LOWER(CONCAT('%', :term, '%'))) ")
    Page<SystemConfigurationTransactionContentProjection> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );


    @Query(value = "select s.id from SystemConfigurationTransactionContentEntity s where s.customerId in :ids")
    List<Long> getListTransactionContentIdByCustomerIds(@Param("ids") List<Long> ids);

    void deleteByCustomerIdIn(List<Long> customerIds);
    
    @Query("select s.id from SystemConfigurationTransactionContentEntity s ")
    List<Long> getListCustomerId ();
}


