package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.model.partner.SearchPartnerRQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository  extends BaseRepository<PartnerEntity, Long> {
    @Query(value = "select p.id from PartnerEntity p " +
            "join SystemConfigurationSourceEntity s on p.id = s.partnerId " +
            "join SystemConfigurationAutoReconciliationEntity r on s.id = r.sourceId " +
            "where r.id = :id")
    Long getPartnerNameByReconciliationId(Long id);

    @Query("SELECT p FROM PartnerEntity p " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:#{#searchRQ.id} IS NULL OR p.id = :#{#searchRQ.id}) " +
            "AND (:#{#searchRQ.name} IS NULL OR p.name = :#{#searchRQ.name}) " +
            "AND (:#{#searchRQ.email} IS NULL OR p.email = :#{#searchRQ.email}) " +
            "AND (:#{#searchRQ.phone} IS NULL OR p.phone = :#{#searchRQ.phone}) " +
            "AND (:#{#searchRQ.status} IS NULL OR p.status = :#{#searchRQ.status}) " +
            "AND (:term IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.phone) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<PartnerEntity> searchPartners(@Param("searchRQ") SearchPartnerRQ searchRQ,
                                       @Param("term") String term,
                                       Pageable pageable);
}
