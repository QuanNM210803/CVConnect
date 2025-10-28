package com.cvconnect.service;

import com.cvconnect.dto.calendar.CalendarCandidateInfoDto;

import java.util.List;

public interface CalendarCandidateInfoService {
    void create(List<CalendarCandidateInfoDto> dtos);
}
