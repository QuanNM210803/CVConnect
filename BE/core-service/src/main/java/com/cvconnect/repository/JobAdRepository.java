package com.cvconnect.repository;

import com.cvconnect.dto.jobAd.JobAdOrgFilterProjection;
import com.cvconnect.dto.jobAd.JobAdOrgFilterRequest;
import com.cvconnect.entity.JobAd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Integer> {

    @Query(value = "SELECT MAX(CAST(SUBSTRING(code, LENGTH(:prefix) + 1) AS BIGINT)) " +
            "FROM job_ad " +
            "WHERE org_id = :orgId AND code LIKE CONCAT(:prefix, '%')",
            nativeQuery = true)
    Long getSuffixCodeMax(Long orgId, String prefix);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Department d " +
            "JOIN Position p ON p.departmentId = d.id AND p.isActive = true " +
            "WHERE d.isActive = true AND d.orgId = :orgId AND p.id = :positionId")
    boolean existsByOrgIdAndPositionId(Long orgId, Long positionId);

    @Query("SELECT ja FROM JobAd ja WHERE ja.id = :id")
    JobAd findById(Long id);

    @Query(value = """
        SELECT distinct
            ja.id as id,
            ja.title AS title,
            p.id AS positionId,
            p.name AS positionName,
            d.id AS departmentId,
            d.name AS departmentName,
            ja.dueDate AS dueDate,
            ja.quantity AS quantity,
            ja.hrContactId AS hrContactId,
            ja.jobAdStatus AS jobAdStatus,
            ja.isPublic AS isPublic,
            ja.keyCodeInternal AS keyCodeInternal,
            ja.createdBy AS createdBy,
            ja.createdAt AS createdAt,
            ja.updatedBy AS updatedBy,
            ja.updatedAt AS updatedAt
        FROM JobAd ja
        JOIN Position p ON ja.positionId = p.id
        JOIN Department d ON p.departmentId = d.id
        JOIN JobAdProcess jap ON jap.jobAdId = ja.id
        LEFT JOIN Calendar c ON c.jobAdProcessId = jap.id
        LEFT JOIN InterviewPanel ip ON ip.calendarId = c.id
        WHERE ja.orgId = :#{#request.orgId}
          AND (:#{#request.keyword} IS NULL OR LOWER(ja.title) LIKE LOWER(CONCAT('%', :#{#request.keyword}, '%')) OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.keyword}, '%')))
          AND (:#{#request.jobAdStatus?.name()} IS NULL OR ja.jobAdStatus = :#{#request.jobAdStatus?.name()})
          AND (:#{#request.isPublic} IS NULL OR ja.isPublic = :#{#request.isPublic})
          AND (:#{#request.departmentIds} IS NULL OR d.id IN :#{#request.departmentIds})
          AND (:#{#request.hrContactId} IS NULL OR ja.hrContactId = :#{#request.hrContactId})
          AND (:#{#request.createdBy} IS NULL OR LOWER(ja.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%')))
          AND (ja.createdAt >= COALESCE(:#{#request.createdAtStart}, ja.createdAt))
          AND (ja.createdAt <= COALESCE(:#{#request.createdAtEnd}, ja.createdAt))
          AND (ja.dueDate >= COALESCE(:#{#request.dueDateStart}, ja.dueDate))
          AND (ja.dueDate <= COALESCE(:#{#request.dueDateEnd}, ja.dueDate))
          AND (:participantId IS NULL OR ja.hrContactId = :participantId OR (ip.interviewerId IS NOT NULL AND ip.interviewerId = :participantId))
    """,
    countQuery = """
        SELECT count(distinct ja.id)
        FROM JobAd ja
        JOIN Position p ON ja.positionId = p.id
        JOIN Department d ON p.departmentId = d.id
        JOIN JobAdProcess jap ON jap.jobAdId = ja.id
        LEFT JOIN Calendar c ON c.jobAdProcessId = jap.id
        LEFT JOIN InterviewPanel ip ON ip.calendarId = c.id
        WHERE ja.orgId = :#{#request.orgId}
          AND (:#{#request.keyword} IS NULL OR LOWER(ja.title) LIKE LOWER(CONCAT('%', :#{#request.keyword}, '%')) OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.keyword}, '%')))
          AND (:#{#request.jobAdStatus?.name()} IS NULL OR ja.jobAdStatus = :#{#request.jobAdStatus?.name()})
          AND (:#{#request.isPublic} IS NULL OR ja.isPublic = :#{#request.isPublic})
          AND (:#{#request.departmentIds} IS NULL OR d.id IN :#{#request.departmentIds})
          AND (:#{#request.hrContactId} IS NULL OR ja.hrContactId = :#{#request.hrContactId})
          AND (:#{#request.createdBy} IS NULL OR LOWER(ja.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%')))
          AND (ja.createdAt >= COALESCE(:#{#request.createdAtStart}, ja.createdAt))
          AND (ja.createdAt <= COALESCE(:#{#request.createdAtEnd}, ja.createdAt))
          AND (ja.dueDate >= COALESCE(:#{#request.dueDateStart}, ja.dueDate))
          AND (ja.dueDate <= COALESCE(:#{#request.dueDateEnd}, ja.dueDate))
          AND (:participantId IS NULL OR ja.hrContactId = :participantId OR (ip.interviewerId IS NOT NULL AND ip.interviewerId = :participantId))
    """
    )
    Page<JobAdOrgFilterProjection> filterJobAdsForOrg(JobAdOrgFilterRequest request, Pageable pageable, Long participantId);

    @Query("select distinct ja from JobAd ja " +
            "join JobAdProcess jap on jap.jobAdId = ja.id " +
            "where jap.id = :jobAdProcessId")
    JobAd findByJobAdProcessId(Long jobAdProcessId);
}
