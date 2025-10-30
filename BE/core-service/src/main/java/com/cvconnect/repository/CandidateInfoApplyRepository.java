package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
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
}
