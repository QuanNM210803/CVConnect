package com.cvconnect.repository;

import com.cvconnect.dto.roleUser.RoleUserProjection;
import com.cvconnect.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    @Query("SELECT ru FROM RoleUser ru WHERE ru.userId = :userId AND ru.roleId = :roleId")
    RoleUser findByUserIdAndRoleId(Long userId, Long roleId);

    @Query("SELECT ru.id AS id, r.id AS roleId, r.name AS roleName, r.code AS roleCode " +
            "FROM RoleUser ru " +
            "JOIN Role r ON r.id = ru.roleId " +
            "WHERE ru.userId = :userId")
    List<RoleUserProjection> findByUserId(Long userId);
}
