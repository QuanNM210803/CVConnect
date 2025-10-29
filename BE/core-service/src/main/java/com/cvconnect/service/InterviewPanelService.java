package com.cvconnect.service;

import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.interviewPanel.InterviewPanelDto;

import java.util.List;

public interface InterviewPanelService {
    void create(List<InterviewPanelDto> dtos);
    List<UserDto> getByCalendarId(Long calendarId);
}
