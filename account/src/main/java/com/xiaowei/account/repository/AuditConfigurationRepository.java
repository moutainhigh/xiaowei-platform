package com.xiaowei.account.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.account.entity.AuditConfiguration;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditConfigurationRepository extends BaseRepository<AuditConfiguration>{
    @Query("select a from AuditConfiguration a where a.userId = ?1 and " +
            "a.departmentId = ?2 and a.typeStatus = ?3")
    AuditConfiguration findByUserIdAndDepartmentIdAndTypeStatus(String userId, String departmentId, Integer typeStatus);

    @Query("select a from AuditConfiguration a where a.typeStatus = ?1")
    List<AuditConfiguration> findByType(Integer status);
}
