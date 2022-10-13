package com.example.batchpjt.repository.user;

import com.example.batchpjt.domain.user.UserGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Integer> {
    List<UserGroupMappingEntity> findByUserGroupId(String userGroupId);
}
