package com.cvconnect.repository;

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
}
