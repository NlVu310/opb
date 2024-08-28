package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.model.customer.SearchCustomerRQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerEntity, Long> {
    List<CustomerEntity> findByIdIn(List<Long> ids);

    @Query(value = "SELECT c FROM CustomerEntity c " +
            "WHERE c.deletedAt IS NULL " +
            "AND (:#{#searchRQ.id} IS NULL OR c.id = :#{#searchRQ.id}) " +
            "AND (:#{#searchRQ.name} IS NULL OR c.name = :#{#searchRQ.name}) " +
            "AND (:#{#searchRQ.taxNo} IS NULL OR c.taxNo = :#{#searchRQ.taxNo}) " +
            "AND (:#{#searchRQ.address} IS NULL OR c.address = :#{#searchRQ.address}) " +
            "AND (:#{#searchRQ.status} IS NULL OR c.status = :#{#searchRQ.status}) " +
            "AND (:#{#searchRQ.code} IS NULL OR c.code = :#{#searchRQ.code}) " +
            "AND (:#{#searchRQ.isParent} IS NULL OR c.isParent = :#{#searchRQ.isParent}) " +
            "AND (:term IS NULL OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.taxNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.address) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR CAST(c.id AS string) LIKE LOWER(CONCAT('%', :term, '%'))) ")
    Page<CustomerEntity> searchCustomers(@Param("searchRQ") SearchCustomerRQ searchRQ,
                                         @Param("term") String term,
                                         Pageable pageable);

}
