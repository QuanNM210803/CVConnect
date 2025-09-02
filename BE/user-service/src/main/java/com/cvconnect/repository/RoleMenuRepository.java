package com.cvconnect.repository;

import com.cvconnect.dto.roleMenu.RoleMenuProjection;
import com.cvconnect.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
    @Query("SELECT DISTINCT rm.id AS roleMenuId, rm.permission AS permission, m.code AS menuCode FROM RoleMenu rm " +
            "JOIN Role r ON rm.roleId = r.id " +
            "JOIN RoleUser ur ON ur.roleId = r.id AND ur.userId = :userId " +
            "JOIN Menu m ON m.id = rm.menuId")
    List<RoleMenuProjection> findAuthoritiesByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM RoleMenu rm WHERE rm.roleId = :roleId")
    void deleteByRoleId(Long roleId);
}
