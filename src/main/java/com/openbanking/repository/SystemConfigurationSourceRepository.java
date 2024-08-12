package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SystemConfigurationSourceRepository  extends BaseRepository<SystemConfigurationSourceEntity, Long> {
}
