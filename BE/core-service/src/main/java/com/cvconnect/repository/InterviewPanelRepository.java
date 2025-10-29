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
}
