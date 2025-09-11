package com.cvconnect.repository;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.role.RoleFilterRequest;
import com.cvconnect.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.code = :code")
    Role findByCode(String code);

    @Modifying
    @Query("DELETE FROM Role r WHERE r.id IN :ids")
    void deleteByIds(List<Long> ids);

    @Query("SELECT new com.cvconnect.dto.role.RoleDto(r.id, r.code, r.name, r.memberType, r.createdAt, r.updatedAt, r.createdBy, r.updatedBy) " +
            "FROM Role r " +
            "WHERE (:#{#request.code} IS NULL OR LOWER(r.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.memberType == null || #request.memberType.isEmpty()} = true OR r.memberType IN :#{#request.memberType}) " +
            "AND (:#{#request.createdAtStart} IS NULL OR r.createdAt >= :#{#request.createdAtStart}) " +
            "AND (:#{#request.createdAtEnd} IS NULL OR r.createdAt <= :#{#request.createdAtEnd}) " +
            "AND (:#{#request.updatedAtStart} IS NULL OR r.updatedAt >= :#{#request.updatedAtStart}) " +
            "AND (:#{#request.updatedAtEnd} IS NULL OR r.updatedAt <= :#{#request.updatedAtEnd}) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(r.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(r.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) "
    )
    Page<RoleDto> filter(@Param("request") RoleFilterRequest request, Pageable pageable);

    @Query("SELECT new com.cvconnect.dto.role.RoleDto(r.id, r.code, r.name, r.memberType, r.createdAt, r.updatedAt, r.createdBy, r.updatedBy) " +
            "FROM Role r " +
            "JOIN RoleUser ru ON ru.roleId = r.id " +
            "WHERE ru.userId = :userId")
    List<RoleDto> getRoleByUserId(Long userId);
}
