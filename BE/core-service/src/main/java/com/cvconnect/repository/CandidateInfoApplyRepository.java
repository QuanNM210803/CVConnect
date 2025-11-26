package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoFilterByJobAdProcess;
import com.cvconnect.entity.CandidateInfoApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateInfoApplyRepository extends JpaRepository<CandidateInfoApply, Long> {

    @Query("SELECT c FROM CandidateInfoApply c " +
            "WHERE :#{#request.candidateId} IS NULL OR c.candidateId = :#{#request.candidateId} ")
    Page<CandidateInfoApply> filter(CandidateInfoApplyFilterRequest request, Pageable pageable);

    @Query("SELECT COUNT(distinct c) FROM CandidateInfoApply c " +
            "join JobAdCandidate jac on jac.candidateInfoId = c.id " +
            "join JobAdProcessCandidate japc on japc.jobAdCandidateId = jac.id " +
            "WHERE c.id IN :candidateInfoIds " +
            "AND japc.jobAdProcessId = :jobAdProcessId " +
            "and japc.isCurrentProcess = true")
    Long countByCandidateInfoIdsAndJobAdProcessId(List<Long> candidateInfoIds, Long jobAdProcessId);

    @Query("SELECT c FROM CandidateInfoApply c " +
            "join CalendarCandidateInfo cci on cci.candidateInfoId = c.id " +
            "WHERE cci.calendarId = :calendarId")
    List<CandidateInfoApply> findByCalendarId(Long calendarId);

    @Query("SELECT distinct c FROM CandidateInfoApply c " +
            "join JobAdCandidate jac on jac.candidateInfoId = c.id " +
            "join JobAdProcessCandidate japc on japc.jobAdCandidateId = jac.id " +
            "WHERE japc.jobAdProcessId = :jobAdProcessId " +
            "and japc.isCurrentProcess = true")
    List<CandidateInfoApply> findCandidateInCurrentProcess(Long jobAdProcessId);

    @Query("""
        select distinct cci.candidateInfoId from CalendarCandidateInfo cci
        join Calendar c on c.id = cci.calendarId
        join JobAdProcess jap on jap.id = c.jobAdProcessId
        where jap.id = :jobAdProcessId and cci.candidateInfoId in :candidateInfoIds
    """)
    List<Long> getCandidateInfoHasSchedule(Long jobAdProcessId, List<Long> candidateInfoIds);

    @Query("""
        select cia.id as id, cia.fullName as fullName, cia.email as email, cia.phone as phone, l.id as levelId, l.name as levelName,
            jac.candidateStatus as candidateStatus, jac.applyDate as applyDate, jac.onboardDate as onboardDate
        from CandidateInfoApply cia
        join JobAdCandidate jac on jac.candidateInfoId = cia.id
        join JobAdProcessCandidate japc on japc.jobAdCandidateId = jac.id
        join JobAdProcess jap on jap.id = japc.jobAdProcessId
        left join CandidateSummaryOrg cso on cso.candidateInfoId = cia.id and cso.orgId = :#{#request.orgId}
        left join Level l on l.id = cso.levelId
        where jap.id = :#{#request.jobAdProcessId}
        and japc.isCurrentProcess = true
        and (:#{#request.fullName} is null or lower(concat('%', cia.fullName, '%')) like :#{#request.fullName})
        and (:#{#request.email} is null or lower(concat('%', cia.email, '%')) like :#{#request.email})
        and (:#{#request.phone} is null or lower(concat('%', cia.phone, '%')) like :#{#request.phone})
        and (:#{#request.levelIds == null || #request.levelIds.isEmpty()} = true or (cso.levelId is not null and cso.levelId in :#{#request.levelIds}))
        and (:#{#request.candidateStatuses == null || #request.candidateStatuses.isEmpty()} = true or jac.candidateStatus in :#{#request.candidateStatuses})
        and (COALESCE(:#{#request.applyDateStart}, NULL) IS NULL OR jac.applyDate >= :#{#request.applyDateStart})
        and (COALESCE(:#{#request.applyDateEnd}, NULL) IS NULL OR jac.applyDate <= :#{#request.applyDateEnd})
        and (COALESCE(:#{#request.onboardDateStart}, NULL) IS NULL OR (jac.onboardDate is not null and jac.onboardDate >= :#{#request.onboardDateStart}))
        and (COALESCE(:#{#request.onboardDateEnd}, NULL) IS NULL OR (jac.onboardDate is not null and jac.onboardDate <= :#{#request.onboardDateEnd}))
    """)
    Page<CandidateInfoApplyProjection> filterByJobAdProcess(CandidateInfoFilterByJobAdProcess request, Pageable pageable);
}
