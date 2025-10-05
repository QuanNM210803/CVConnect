package com.cvconnect.repository;

import com.cvconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.accessMethod LIKE '%LOCAL%'")
    Optional<User> findByUsernameLogin(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(ru) > 0 THEN true ELSE false END " +
            "FROM RoleUser ru " +
            "JOIN Role r ON ru.roleId = r.id " +
            "JOIN OrgMember om ON ru.userId = om.userId AND om.isActive = true " +
            "WHERE ru.userId = :userId AND r.code = :roleCode AND om.orgId = :orgId")
    Boolean checkOrgUserRole(Long userId, String roleCode, Long orgId);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN RoleUser ru ON u.id = ru.userId " +
            "JOIN Role r ON ru.roleId = r.id " +
            "JOIN OrgMember om ON ru.userId = om.userId AND (:active IS NULL OR om.isActive = :active) " +
            "WHERE r.code = :roleCode AND om.orgId = :orgId"
    )
    List<User> getUsersByRoleCodeOrg(String roleCode, Long orgId, Boolean active);
}
