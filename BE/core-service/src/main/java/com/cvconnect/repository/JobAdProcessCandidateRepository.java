package com.cvconnect.repository;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.JobAdProcessCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdProcessCandidateRepository extends JpaRepository<JobAdProcessCandidate, Long> {
    @Query("SELECT jpc FROM JobAdProcessCandidate jpc " +
            "JOIN JobAdProcess jap ON jap.id = jpc.jobAdProcessId " +
            "WHERE jpc.jobAdCandidateId = :jobAdCandidateId " +
            "ORDER BY jap.sortOrder ASC")
    List<JobAdProcessCandidate> findByJobAdCandidateId(Long jobAdCandidateId);

    @Query("""
        SELECT CASE WHEN COUNT(japc) > 0 THEN TRUE ELSE FALSE END
        FROM JobAdProcessCandidate japc
        JOIN JobAdProcess jap ON jap.id = japc.jobAdProcessId
        WHERE japc.id = :jobAdProcessCandidateId
          AND jap.sortOrder > (
              SELECT jap2.sortOrder
              FROM JobAdProcessCandidate japc2
              JOIN JobAdProcess jap2 ON jap2.id = japc2.jobAdProcessId
              WHERE japc2.jobAdCandidateId = :jobAdCandidateId
              AND japc2.isCurrentProcess = true
              order by jap2.sortOrder desc limit 1
          )
    """)
    Boolean validateProcessOrderChange(Long jobAdProcessCandidateId, Long jobAdCandidateId);

    @Query("""
        select case when count(*) > 0 then true else false end
        from JobAdProcessCandidate as japc
        join JobAdProcess as jap on jap.id = japc.jobAdProcessId
        join ProcessType as pt on pt.id = jap.processTypeId
        where japc.jobAdCandidateId = :jobAdCandidateId and japc.isCurrentProcess = true
            and pt.code = :processTypeCode
    """)
    Boolean validateCurrentProcessTypeIs(Long jobAdCandidateId, String processTypeCode);

    @Query("select new com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto(jap.id, jac.id, japc.actionDate, japc.isCurrentProcess, jap.name) from JobAdProcessCandidate japc " +
            "join JobAdCandidate jac on jac.id = japc.jobAdCandidateId " +
            "join JobAdProcess jap on jap.id = japc.jobAdProcessId " +
            "where jac.jobAdId = :jobAdId and jac.candidateInfoId = :candidateInfoId and japc.isCurrentProcess = true")
    JobAdProcessCandidateDto getCurrentProcess(Long jobAdId, Long candidateInfoId);
}
