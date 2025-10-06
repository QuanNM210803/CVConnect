package com.cvconnect.repository;

import com.cvconnect.entity.JobAdCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdCandidateRepository extends JpaRepository<JobAdCandidate, Long> {
}
