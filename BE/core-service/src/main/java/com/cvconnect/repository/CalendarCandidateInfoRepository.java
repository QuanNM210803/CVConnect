package com.cvconnect.repository;

import com.cvconnect.entity.CalendarCandidateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarCandidateInfoRepository extends JpaRepository<CalendarCandidateInfo, Long> {
    @Query("select cci from CalendarCandidateInfo cci where cci.calendarId = :calendarId and cci.candidateInfoId = :candidateInfoId")
    CalendarCandidateInfo findByCalendarIdAndCandidateInfoId(Long calendarId, Long candidateInfoId);
}
