package com.cvconnect.dto.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CalendarDetailInViewCandidateProjection {
    Long getJobAdId();
    String getJobAdTitle();
    Long getJobAdProcessId();
    String getJobAdProcessName();
    Long getCreatorId();
    String getCalendarType();
    LocalDate getDate();
    LocalTime getTimeTo();
    LocalTime getTimeFrom();
    Long getLocationId();
    String getMeetingLink();
    Long getCalendarId();
}
