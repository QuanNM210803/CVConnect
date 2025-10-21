package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterProjection;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterRequest;
import com.cvconnect.entity.JobAdCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdCandidateRepository extends JpaRepository<JobAdCandidate, Long> {

    @Query("SELECT CASE WHEN COUNT(jc) > 0 THEN true ELSE false END " +
            "FROM JobAdCandidate jc " +
            "JOIN CandidateInfoApply cia ON cia.id = jc.candidateInfoId " +
            "WHERE jc.jobAdId = :jobAdId AND cia.candidateId = :candidateId")
    boolean existsByJobAdIdAndCandidateId(Long jobAdId, Long candidateId);


    @Query(value = """
        select cia.id as id, cia.full_name as fullName, cia.email as email, cia.phone as phone,
               count(distinct jac.job_ad_id) as numOfApply
        from candidate_info_apply as cia
        join job_ad_candidate as jac on jac.candidate_info_id = cia.id
        join job_ad as ja on ja.id = jac.job_ad_id
        join job_ad_process_candidate as japc on japc.job_ad_candidate_id = jac.id and japc.is_current_process = true
        join job_ad_process as jap on jap.id = japc.job_ad_process_id
        where (:#{#request.fullName} is null or lower(cia.full_name) like lower(concat('%', :#{#request.fullName}, '%')))
        and (:#{#request.email} is null or lower(cia.email) like lower(concat('%', :#{#request.email}, '%')))
        and (:#{#request.phone} is null or lower(cia.phone) like lower(concat('%', :#{#request.phone}, '%')))
        and (:#{#request.jobAdTitle} is null or lower(ja.title) like lower(concat('%', :#{#request.jobAdTitle}, '%')))
        and (:#{#request.candidateStatuses} is null or jac.candidate_status in :#{#request.candidateStatuses})
        and (:#{#request.processTypes} is null or jap.process_type_id in :#{#request.processTypes})
        and (COALESCE(:#{#request.applyDateStart}, NULL) IS NULL OR jac.apply_date >= :#{#request.applyDateStart})
        and (COALESCE(:#{#request.applyDateEnd}, NULL) IS NULL OR jac.apply_date <= :#{#request.applyDateEnd})
        and (:#{#request.hrContactId} is null or ja.hr_contact_id = :#{#request.hrContactId})
        and (:participantId is null or ja.hr_contact_id = :participantId)
        and (:orgId is null or ja.org_id = :orgId)
        group by cia.id, cia.full_name, cia.email, cia.phone
        having (:#{#request.numOfApplyStart} is null or count(distinct jac.job_ad_id) >= :#{#request.numOfApplyStart})
            and (:#{#request.numOfApplyEnd} is null or count(distinct jac.job_ad_id) <= :#{#request.numOfApplyEnd})
    """ , nativeQuery = true)
    Page<CandidateInfoApplyProjection> filter(CandidateFilterRequest request, Long orgId, Long participantId, Pageable pageable);

    List<CandidateFilterProjection> findAllByCandidateInfoIds(List<Long> candidateInfoIds, Long orgId, Long participantId);
}
