package com.cvconnect.repository;

import com.cvconnect.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    @Query("SELECT ru FROM RoleUser ru WHERE ru.userId = :userId AND ru.roleId = :roleId AND ru.isDeleted = false")
    RoleUser findByUserIdAndRoleId(Long userId, Long roleId);
}
