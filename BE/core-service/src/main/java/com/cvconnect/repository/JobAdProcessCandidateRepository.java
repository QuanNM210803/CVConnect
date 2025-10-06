package com.cvconnect.repository;

import com.cvconnect.entity.JobAdProcessCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdProcessCandidateRepository extends JpaRepository<JobAdProcessCandidate, Long> {
}
