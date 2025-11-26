package com.cvconnect.repository;

import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationProjection;
import com.cvconnect.entity.CandidateEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateEvaluationRepository extends JpaRepository<CandidateEvaluation, Long> {
    @Query("""
        select distinct(ip.interviewerId) from InterviewPanel ip
        join Calendar c on c.id = ip.calendarId
        join CalendarCandidateInfo cci on cci.calendarId = c.id
        where c.jobAdProcessId = :jobAdProcessId and cci.candidateInfoId = :candidateInfoId
    """)
    List<Long> getInterviewerByJobAdProcessAndCandidateInfoId(Long jobAdProcessId, Long candidateInfoId);

    @Query("""
        select distinct jap.id as jobAdProcessId, jap.name as jobAdProcessName, jap.sortOrder as sortOrder,
                    ce.id as id, ce.evaluatorId as evaluatorId, ce.comments as comments, ce.score as score, ce.createdAt as createdAt, ce.updatedAt as updatedAt
        from CandidateEvaluation ce
        join JobAdProcessCandidate japc on japc.id = ce.jobAdProcessCandidateId
        join JobAdProcess jap on jap.id = japc.jobAdProcessId
        where japc.jobAdCandidateId = :jobAdCandidateId
        and (:evaluatorId is null or ce.evaluatorId = :evaluatorId)
        order by sortOrder asc, createdAt asc
    """)
    List<CandidateEvaluationProjection> getByJobAdCandidateId(Long jobAdCandidateId, Long evaluatorId);
}
