package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerEntity, Long> {
    List<CustomerEntity> findByIdIn(List<Long> ids);
}
