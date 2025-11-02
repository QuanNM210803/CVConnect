package com.cvconnect.repository;

import com.cvconnect.entity.InterviewPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewPanelRepository extends JpaRepository<InterviewPanel, Long> {
    @Query("SELECT ip FROM InterviewPanel ip WHERE ip.calendarId = :calendarId")
    List<InterviewPanel> findByCalendarId(Long calendarId);

    @Query("SELECT CASE WHEN COUNT(ip) > 0 THEN true ELSE false END " +
            "FROM InterviewPanel ip " +
            "JOIN Calendar c ON c.id = ip.calendarId " +
            "JOIN JobAdProcess jap ON jap.id = c.jobAdProcessId " +
            "WHERE jap.jobAdId = :jobAdId AND ip.interviewerId = :interviewerId")
    Boolean existsByJobAdIdAndInterviewerId(Long jobAdId, Long interviewerId);
}
