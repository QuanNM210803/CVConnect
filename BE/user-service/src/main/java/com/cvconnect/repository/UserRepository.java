package com.cvconnect.repository;

import com.cvconnect.dto.user.UserFilterRequest;
import com.cvconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT u FROM User u
        WHERE
        (:#{#request.memberTypes} IS NULL OR u.id NOT IN (
            SELECT ru1.userId FROM RoleUser ru1
            JOIN Role r1 ON ru1.roleId = r1.id
            WHERE r1.memberType IN :#{#request.memberTypes}
        ))
        AND (:#{#request.email} IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :#{#request.email}, '%')))
    """)
    Page<User> findNotOrgMember(UserFilterRequest request, Pageable pageable);

}
