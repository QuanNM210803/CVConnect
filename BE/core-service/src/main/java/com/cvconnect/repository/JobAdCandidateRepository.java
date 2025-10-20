package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterRequest;
import com.cvconnect.entity.JobAdCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdCandidateRepository extends JpaRepository<JobAdCandidate, Long> {

    @Query("SELECT CASE WHEN COUNT(jc) > 0 THEN true ELSE false END " +
            "FROM JobAdCandidate jc " +
            "JOIN CandidateInfoApply cia ON cia.id = jc.candidateInfoId " +
            "WHERE jc.jobAdId = :jobAdId AND cia.candidateId = :candidateId")
    boolean existsByJobAdIdAndCandidateId(Long jobAdId, Long candidateId);


    Page<CandidateInfoApplyProjection> filter(
            CandidateFilterRequest request,
            Long orgId,
            Long participantId,
            Pageable pageable
    );
}
