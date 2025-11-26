package com.cvconnect.dto.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CalendarFilterViewCandidateProjection {
    LocalDate getDate();
    Long getCalendarId();
    Long getCalendarCandidateInfoId();
    Long getJobAdProcessId();
    String getJobAdProcessName();
    Long getCreatorId();
    String getCalendarType();
    LocalTime getTimeFrom();
    LocalTime getTimeTo();
}
