package com.cvconnect.service;

import com.cvconnect.dto.calendar.CalendarCandidateInfoDto;

import java.util.List;

public interface CalendarCandidateInfoService {
    void create(List<CalendarCandidateInfoDto> dtos);
    CalendarCandidateInfoDto getByCalendarIdAndCandidateInfoId(Long calendarId, Long candidateInfoId);
}
