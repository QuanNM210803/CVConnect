package com.cvconnect.repository;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessProjection;
import com.cvconnect.entity.JobAdProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdProcessRepository extends JpaRepository<JobAdProcess, Long> {
    @Query("SELECT j FROM JobAdProcess j WHERE j.jobAdId = :jobAdId")
    List<JobAdProcess> findByJobAdId(Long jobAdId);

    @Query("SELECT CASE WHEN COUNT(jap) > 0 THEN true ELSE false END FROM JobAdProcess jap " +
           "join JobAd ja on ja.id = jap.jobAdId " +
           "WHERE jap.id = :jobAdProcessId AND ja.orgId = :orgId")
    Boolean existByJobAdProcessIdAndOrgId(Long jobAdProcessId, Long orgId);

    @Query("""
        select jap.id as id, jap.name as processName, jap.jobAdId as jobAdId, jap.sortOrder as sortOrder, count(japc.id) as numberOfApplicants
        from JobAdProcess jap
        left join JobAdProcessCandidate japc on japc.jobAdProcessId = jap.id and japc.isCurrentProcess = true
        where jap.jobAdId in :jobAdIds
        group by jap.id, jap.name, jap.jobAdId, jap.sortOrder
        order by jobAdId asc, sortOrder asc
    """)
    List<JobAdProcessProjection> findJobAdProcessByJobAdIds(List<Long> jobAdIds);
}
