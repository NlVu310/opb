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

    @Query("SELECT LOWER(p.name) FROM PartnerEntity p WHERE p.deletedAt IS NULL")
    List<String> findNames();

    @Query("SELECT p.code FROM PartnerEntity p WHERE p.deletedAt IS NULL")
    List<String> findCodes();

    @Query(value = "select p.name from PartnerEntity p where p.id = :id and p.deletedAt is null")
    String getPartnerNameById(Long id);

    @Query(value = "select p.id from PartnerEntity p " +
            "where p.deletedAt is null ")
    List<Long> getListPartnerId();

    @Query("SELECT p FROM PartnerEntity p " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:#{#searchRQ.name} IS NULL OR p.name = :#{#searchRQ.name}) " +
            "AND (:#{#searchRQ.email} IS NULL OR p.email = :#{#searchRQ.email}) " +
            "AND (:#{#searchRQ.phone} IS NULL OR p.phone = :#{#searchRQ.phone}) " +
            "AND (:#{#searchRQ.status} IS NULL OR p.status = :#{#searchRQ.status}) " +
            "AND (:#{#searchRQ.code} IS NULL OR p.code = :#{#searchRQ.code}) " +
            "AND (:term IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.code) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.phone) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "AND p.id IN :ids ")
    Page<PartnerEntity> searchPartners(@Param("searchRQ") SearchPartnerRQ searchRQ,
                                       @Param("term") String term,
                                       @Param("ids") List<Long> ids,
                                       Pageable pageable);

    @Query("SELECT DISTINCT LOWER(p.name) FROM PartnerEntity p WHERE p.id <> :id AND p.deletedAt IS NULL")
    List<String> findDistinctLowercasePartnerNamesExcluding(Long id);

    List<PartnerEntity> findByIdIn(List<Long> ids);
}
