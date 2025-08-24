package com.cvconnect.repository;

import com.cvconnect.dto.menu.MenuProjection;
import com.cvconnect.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m.id AS id, m.code AS menuCode, m.label AS menuLabel, " +
            "m.icon AS menuIcon, m.url AS menuUrl, m.parentId AS parentId, m.sortOrder AS menuSortOrder, rm.permission AS permission " +
            "FROM RoleMenu rm " +
            "JOIN Menu m ON m.id = rm.menuId " +
            "WHERE rm.roleId = :roleId AND rm.isActive = true AND m.isActive = true " +
            "ORDER BY m.sortOrder ASC, m.id ASC")
    List<MenuProjection> findMenusByRoleId(Long roleId);
}
