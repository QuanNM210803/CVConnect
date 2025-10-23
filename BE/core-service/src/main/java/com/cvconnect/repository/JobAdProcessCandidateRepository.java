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


    // todo: viet query
    @Query
    Boolean validateProcessOrderChange(Long jobAdProcessCandidateId, Long jobAdCandidateId);
}
