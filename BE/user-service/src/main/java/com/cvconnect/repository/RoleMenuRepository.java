package com.cvconnect.repository;

import com.cvconnect.dto.RoleMenuProjection;
import com.cvconnect.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
    @Query("SELECT DISTINCT rm.id AS roleMenuId, rm.permission AS permission, m.code AS menuCode FROM RoleMenu rm " +
            "JOIN Role r ON rm.roleId = r.id AND r.isDeleted = false " +
            "JOIN RoleUser ur ON ur.roleId = r.id AND ur.userId = :userId AND ur.isDeleted = false " +
            "JOIN Menu m ON m.id = rm.menuId AND m.isDeleted = false " +
            "WHERE rm.isDeleted = false ")
    List<RoleMenuProjection> findAuthoritiesByUserId(Long userId);
}
