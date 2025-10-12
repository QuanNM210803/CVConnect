package com.cvconnect.repository;

import com.cvconnect.dto.orgMember.OrgMemberFilter;
import com.cvconnect.dto.orgMember.OrgMemberProjection;
import com.cvconnect.entity.OrgMember;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgMemberRepository extends JpaRepository<OrgMember, Long> {
    boolean existsByUserId(@NotNull Long userId);

    Optional<OrgMember> findByUserId(@NotNull Long userId);

    @Query( value = "select distinct u.id as userId, u.username as username, u.email as email, u.fullName as fullName, u.phoneNumber as phoneNumber, u.dateOfBirth as dateOfBirth," +
            "u.isEmailVerified as isEmailVerified, u.isActive as isActive, om.createdAt as createdAt, om.updatedAt as updatedAt," +
            "om.inviter as inviter, om.updatedBy as updatedBy " +
            "from User u " +
            "join OrgMember om on u.id = om.userId " +
            "join RoleUser ru on ru.userId = u.id " +
            "where (:#{#request.orgId} is null or om.orgId = :#{#request.orgId}) " +
            "and (:#{#request.username} is null or lower(u.username) like lower(concat('%', :#{#request.username}, '%'))) " +
            "and (:#{#request.email} is null or lower(u.email) like lower(concat('%', :#{#request.email}, '%'))) " +
            "and (:#{#request.fullName} is null or lower(u.fullName) like lower(concat('%', :#{#request.fullName}, '%'))) " +
            "and (:#{#request.phoneNumber} is null or lower(u.phoneNumber) like lower(concat('%', :#{#request.phoneNumber}, '%'))) " +
            "AND (COALESCE(:#{#request.dateOfBirthStart}, NULL) IS NULL OR (u.dateOfBirth IS NOT NULL AND u.dateOfBirth >= :#{#request.dateOfBirthStart})) " +
            "AND (COALESCE(:#{#request.dateOfBirthEnd}, NULL) IS NULL OR (u.dateOfBirth IS NOT NULL AND u.dateOfBirth <= :#{#request.dateOfBirthEnd})) " +
            "and (:#{#request.isEmailVerified} is null or u.isEmailVerified = :#{#request.isEmailVerified}) " +
            "and (:#{#request.isActive} is null or om.isActive = :#{#request.isActive}) " +
            "and (:#{#request.roleIds == null || #request.roleIds.isEmpty()} = true or ru.roleId in :#{#request.roleIds}) " +
            "AND (om.createdAt >= COALESCE(:#{#request.createdAtStart}, om.createdAt)) " +
            "AND (om.createdAt <= COALESCE(:#{#request.createdAtEnd}, om.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (om.updatedAt IS NOT NULL AND om.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (om.updatedAt IS NOT NULL AND om.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.inviter} IS NULL OR LOWER(om.inviter) LIKE LOWER(CONCAT('%', :#{#request.inviter}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(om.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) "
            ,
            countQuery = "select count(distinct u.id) " +
            "from User u " +
            "join OrgMember om on u.id = om.userId " +
            "join RoleUser ru on ru.userId = u.id " +
            "where (:#{#request.orgId} is null or om.orgId = :#{#request.orgId}) " +
            "and (:#{#request.username} is null or lower(u.username) like lower(concat('%', :#{#request.username}, '%'))) " +
            "and (:#{#request.email} is null or lower(u.email) like lower(concat('%', :#{#request.email}, '%'))) " +
            "and (:#{#request.fullName} is null or lower(u.fullName) like lower(concat('%', :#{#request.fullName}, '%'))) " +
            "and (:#{#request.phoneNumber} is null or lower(u.phoneNumber) like lower(concat('%', :#{#request.phoneNumber}, '%'))) " +
            "AND (COALESCE(:#{#request.dateOfBirthStart}, NULL) IS NULL OR (u.dateOfBirth IS NOT NULL AND u.dateOfBirth >= :#{#request.dateOfBirthStart})) " +
            "AND (COALESCE(:#{#request.dateOfBirthEnd}, NULL) IS NULL OR (u.dateOfBirth IS NOT NULL AND u.dateOfBirth <= :#{#request.dateOfBirthEnd})) " +
            "and (:#{#request.isEmailVerified} is null or u.isEmailVerified = :#{#request.isEmailVerified}) " +
            "and (:#{#request.isActive} is null or om.isActive = :#{#request.isActive}) " +
            "and (:#{#request.roleIds == null || #request.roleIds.isEmpty()} = true or ru.roleId in :#{#request.roleIds}) " +
            "AND (om.createdAt >= COALESCE(:#{#request.createdAtStart}, om.createdAt)) " +
            "AND (om.createdAt <= COALESCE(:#{#request.createdAtEnd}, om.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (om.updatedAt IS NOT NULL AND om.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (om.updatedAt IS NOT NULL AND om.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.inviter} IS NULL OR LOWER(om.inviter) LIKE LOWER(CONCAT('%', :#{#request.inviter}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(om.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) "
    )
    Page<OrgMemberProjection> filter(OrgMemberFilter request, Pageable pageable);

    @Query("select om from OrgMember om where om.userId in :userIds and om.orgId = :orgId")
    List<OrgMember> findByIdsAndOrgId(List<Long> userIds, Long orgId);
}
