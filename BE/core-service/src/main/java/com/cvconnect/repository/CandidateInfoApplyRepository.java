package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import com.cvconnect.entity.CandidateInfoApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateInfoApplyRepository extends JpaRepository<CandidateInfoApply, Long> {

    @Query("SELECT c FROM CandidateInfoApply c " +
            "WHERE :#{#request.candidateId} IS NULL OR c.candidateId = :#{#request.candidateId} ")
    Page<CandidateInfoApply> filter(CandidateInfoApplyFilterRequest request, Pageable pageable);
}
