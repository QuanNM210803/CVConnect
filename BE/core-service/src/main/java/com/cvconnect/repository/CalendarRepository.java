package com.cvconnect.repository;

import com.cvconnect.dto.calendar.CalendarDetailInViewCandidateProjection;
import com.cvconnect.dto.calendar.CalendarFilterViewCandidateProjection;
import com.cvconnect.dto.calendar.CalendarFitterRequest;
import com.cvconnect.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("""
        select distinct cci.date as date, c.id as calendarId, cci.id as calendarCandidateInfoId,
            jap.id as jobAdProcessId, jap.name as jobAdProcessName, c.creatorId as creatorId, c.calendarType as calendarType,
            cci.timeFrom as timeFrom, cci.timeTo as timeTo from Calendar c
        join CalendarCandidateInfo cci on cci.calendarId = c.id
        join CandidateInfoApply cia on cia.id = cci.candidateInfoId
        join JobAdCandidate jac on jac.candidateInfoId = cia.id
        join JobAd ja on ja.id = jac.jobAdId
        join JobAdProcess jap on jap.id = c.jobAdProcessId
        join InterviewPanel ip on ip.calendarId = c.id
        where jac.id = :#{#request.jobAdCandidateId}
        and (:creatorId is null or c.creatorId = :creatorId)
        and (:participantId is null or ja.hrContactId = :participantId or ip.interviewerId = :participantId)
        and (:participantIdAuth is null or ip.interviewerId = :participantIdAuth)
        order by date desc, timeFrom desc, timeTo asc
    """)
    List<CalendarFilterViewCandidateProjection> filterViewCandidateCalendars(CalendarFitterRequest request, Long creatorId, Long participantId, Long participantIdAuth);

    @Query("""
        select distinct ja.id as jobAdId, ja.title as jobAdTitle, jap.id as jobAdProcessId, jap.name as jobAdProcessName,
            c.creatorId as creatorId, c.calendarType as calendarType, cci.date as date, cci.timeFrom as timeFrom, cci.timeTo as timeTo,
            c.orgAddressId as locationId, c.meetingLink as meetingLink, c.id as calendarId
        from CalendarCandidateInfo cci
        join Calendar c on c.id = cci.calendarId
        join JobAdProcess jap on jap.id = c.jobAdProcessId
        join JobAd ja on ja.id = jap.jobAdId
        join InterviewPanel ip on ip.calendarId = c.id
        where cci.id = :calendarCandidateInfoId
        and ja.orgId = :orgId
        and (:userId is null or c.creatorId = :userId or ja.hrContactId = :userId or ip.interviewerId = :userId)
    """)
    CalendarDetailInViewCandidateProjection detailInViewCandidate(Long calendarCandidateInfoId, Long orgId, Long userId);
}
