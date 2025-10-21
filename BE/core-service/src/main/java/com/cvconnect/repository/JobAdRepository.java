package com.cvconnect.repository;

import com.cvconnect.entity.JobAd;
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
}
