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
        select cia.id as id, cia.fullName as fullName, cia.email as email, cia.phone as phone,
               count(distinct jac.jobAdId) as numOfApply, max(jac.applyDate) as applyDate
        from CandidateInfoApply as cia
        join JobAdCandidate as jac on jac.candidateInfoId = cia.id
        join JobAd as ja on ja.id = jac.jobAdId
        join JobAdProcessCandidate as japc on japc.jobAdCandidateId = jac.id and japc.isCurrentProcess = true
        join JobAdProcess as jap on jap.id = japc.jobAdProcessId
        where
        (:#{#request.fullName} is null or lower(cia.fullName) like lower(concat('%', :#{#request.fullName}, '%')))
        and (:#{#request.email} is null or lower(cia.email) like lower(concat('%', :#{#request.email}, '%')))
        and (:#{#request.phone} is null or lower(cia.phone) like lower(concat('%', :#{#request.phone}, '%')))
        and (:#{#request.jobAdTitle} is null or lower(ja.title) like lower(concat('%', :#{#request.jobAdTitle}, '%')))
        and (:#{#request.candidateStatuses == null || #request.candidateStatuses.empty()} = true or jac.candidateStatus in :#{#request.candidateStatuses})
        and (:#{#request.processTypes ==  null || #request.processTypes.empty()} = true or jap.processTypeId in :#{#request.processTypes})
        and (COALESCE(:#{#request.applyDateStart}, NULL) IS NULL OR jac.applyDate >= :#{#request.applyDateStart})
        and (COALESCE(:#{#request.applyDateEnd}, NULL) IS NULL OR jac.applyDate <= :#{#request.applyDateEnd})
        and (:#{#request.hrContactId} is null or ja.hrContactId = :#{#request.hrContactId})
        and (:participantId is null or ja.hrContactId = :participantId)
        and (:orgId is null or ja.orgId = :orgId)
        group by cia.id, cia.fullName, cia.email, cia.phone
        having (:#{#request.numOfApplyStart} is null or count(distinct jac.jobAdId) >= :#{#request.numOfApplyStart})
            and (:#{#request.numOfApplyEnd} is null or count(distinct jac.jobAdId) <= :#{#request.numOfApplyEnd})
    """ )
    Page<CandidateInfoApplyProjection> filter(CandidateFilterRequest request, Long orgId, Long participantId, Pageable pageable);

    @Query(value = """
        select distinct jac.candidate_info_id as candidateInfoId,
                        ja.id as jobAdId,
                        ja.title as jobAdTitle,
                        jac.candidate_status as candidateStatus,
                        pt.id as processTypeId,
                        pt.name as processTypeName,
                        pt.code as processTypeCode,
                        jac.apply_date as applyDate,
                        ja.hr_contact_id as hrContactId
        from job_ad_candidate as jac
        join job_ad as ja on ja.id = jac.job_ad_id
        join job_ad_process_candidate as japc on japc.job_ad_candidate_id = jac.id and japc.is_current_process = true
        join job_ad_process as jap on jap.id = japc.job_ad_process_id
        join process_type as pt on pt.id = jap.process_type_id
        where (:#{#candidateInfoIds == null || #candidateInfoIds.isEmpty()} = true or jac.candidate_info_id in :candidateInfoIds)
        and (:participantId is null or ja.hr_contact_id = :participantId)
        and (:orgId is null or ja.org_id = :orgId)
    """ , nativeQuery = true)
    List<CandidateFilterProjection> findAllByCandidateInfoIds(List<Long> candidateInfoIds, Long orgId, Long participantId);
}
