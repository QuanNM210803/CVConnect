package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterProjection;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterRequest;
import com.cvconnect.dto.jobAdCandidate.JobAdCandidateProjection;
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
        select cia.id as id, cia.fullName as fullName, cia.email as email, cia.phone as phone, l.id as levelId, l.name as levelName,
               count(distinct jac.jobAdId) as numOfApply, max(jac.applyDate) as applyDate
        from CandidateInfoApply as cia
        join JobAdCandidate as jac on jac.candidateInfoId = cia.id
        join JobAd as ja on ja.id = jac.jobAdId
        join JobAdProcessCandidate as japc on japc.jobAdCandidateId = jac.id and japc.isCurrentProcess = true
        join JobAdProcess as jap on jap.id = japc.jobAdProcessId
        left join CandidateSummaryOrg as cso on cso.candidateInfoId = cia.id and cso.orgId = :orgId
        left join Level l on l.id = cso.levelId
        where
        (:#{#request.fullName} is null or lower(cia.fullName) like lower(concat('%', :#{#request.fullName}, '%')))
        and (:#{#request.email} is null or lower(cia.email) like lower(concat('%', :#{#request.email}, '%')))
        and (:#{#request.phone} is null or lower(cia.phone) like lower(concat('%', :#{#request.phone}, '%')))
        and (:#{#request.levelIds == null || #request.levelIds.empty()} = true or (cso.levelId is not null and cso.levelId in :#{#request.levelIds}))
        and (:#{#request.jobAdTitle} is null or lower(ja.title) like lower(concat('%', :#{#request.jobAdTitle}, '%')))
        and (:#{#request.candidateStatuses == null || #request.candidateStatuses.empty()} = true or jac.candidateStatus in :#{#request.candidateStatuses})
        and (:#{#request.processTypes ==  null || #request.processTypes.empty()} = true or jap.processTypeId in :#{#request.processTypes})
        and (COALESCE(:#{#request.applyDateStart}, NULL) IS NULL OR jac.applyDate >= :#{#request.applyDateStart})
        and (COALESCE(:#{#request.applyDateEnd}, NULL) IS NULL OR jac.applyDate <= :#{#request.applyDateEnd})
        and (:#{#request.hrContactId} is null or ja.hrContactId = :#{#request.hrContactId})
        and (:participantId is null or ja.hrContactId = :participantId)
        and (:orgId is null or ja.orgId = :orgId)
        group by cia.id, cia.fullName, cia.email, cia.phone, l.id, l.name
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

    @Query(value = """
        select distinct cia.id as id, cia.fullName as fullName, cia.email as email, cia.phone as phone, cia.coverLetter as coverLetter,
               af.id as cvFileId, af.secureUrl as cvFileUrl,
               l.id as levelId, l.name as levelName,
               cso.skill as skill
        from CandidateInfoApply as cia
        join JobAdCandidate as jac on jac.candidateInfoId = cia.id
        join JobAd as ja on ja.id = jac.jobAdId
        join AttachFile as af on af.id = cia.cvFileId
        left join CandidateSummaryOrg as cso on cso.candidateInfoId = cia.id and cso.orgId = :orgId
        left join Level as l on l.id = cso.levelId
        where cia.id = :candidateInfoId
        and (:orgId is null or ja.orgId = :orgId)
        and (:participantId is null or ja.hrContactId = :participantId)
    """)
    CandidateInfoApplyProjection getCandidateInfoDetailProjection(Long candidateInfoId, Long orgId, Long participantId);

    @Query(value = """
        select ja.id as jobAdId, ja.title as jobAdTitle, ja.hrContactId as hrContactId,
               p.id as positionId, p.name as positionName,
               d.id as departmentId, d.name as departmentName,
               jac.id as jobAdCandidateId,
               jac.candidateStatus as candidateStatus,
               jac.applyDate as applyDate,
               japc.id as jobAdProcessCandidateId,
               japc.actionDate as actionDate,
               japc.isCurrentProcess as isCurrentProcess,
               jap.name as processName
        from JobAdCandidate as jac
        join JobAd as ja on ja.id = jac.jobAdId
        join Position as p on p.id = ja.positionId
        join Department as d on d.id = p.departmentId
        join JobAdProcessCandidate as japc on japc.jobAdCandidateId = jac.id
        join JobAdProcess as jap on jap.id = japc.jobAdProcessId
        where jac.candidateInfoId = :candidateInfoId
        and (:orgId is null or ja.orgId = :orgId)
        and (:participantId is null or ja.hrContactId = :participantId)
        order by jac.applyDate desc, jap.sortOrder
    """)
    List<JobAdCandidateProjection> getJobAdCandidatesByCandidateInfoId(Long candidateInfoId, Long orgId, Long participantId);

    @Query("""
            select case when count(*) > 0 then true else false end
            from JobAdCandidate as jac
            join JobAd as ja on ja.id = jac.jobAdId
            where jac.candidateInfoId = :candidateInfoId
            and ja.orgId = :orgId
            and (:hrContactId is null or ja.hrContactId = :hrContactId)
        """)
    boolean existsByCandidateInfoIdAndOrgIdAndHrContactId(Long candidateInfoId, Long orgId, Long hrContactId);

    @Query("""
        select case when count(*) > 0 then true else false end
            from JobAdProcessCandidate as japc
            join JobAdCandidate as jac on jac.id = japc.jobAdCandidateId
            join JobAd as ja on ja.id = jac.jobAdId
            where ja.hrContactId = :hrContactId and japc.id = :jobAdProcessCandidateId
    """)
    Boolean existsByJobAdProcessCandidateIdAndHrContactId(Long jobAdProcessCandidateId, Long hrContactId);
}
