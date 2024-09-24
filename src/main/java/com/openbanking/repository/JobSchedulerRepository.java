package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.JobSchedulerEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSchedulerRepository extends BaseRepository<JobSchedulerEntity, Long> {
}
