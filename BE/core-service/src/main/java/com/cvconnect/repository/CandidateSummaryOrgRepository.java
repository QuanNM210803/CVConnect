package com.cvconnect.repository;

import com.cvconnect.entity.CandidateSummaryOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSummaryOrgRepository extends JpaRepository<CandidateSummaryOrg, Long> {
    @Query("SELECT cso FROM CandidateSummaryOrg cso WHERE cso.candidateInfoId = :candidateInfoId AND cso.orgId = :orgId")
    CandidateSummaryOrg findByCandidateInfoIdAndOrgId(Long candidateInfoId, Long orgId);
}
