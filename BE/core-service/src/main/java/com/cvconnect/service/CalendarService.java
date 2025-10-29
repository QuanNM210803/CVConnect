package com.cvconnect.service;

import com.cvconnect.dto.calendar.CalendarFitterRequest;
import com.cvconnect.dto.calendar.CalendarFitterViewCandidateResponse;
import com.cvconnect.dto.calendar.CalendarRequest;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface CalendarService {
    IDResponse<Long> createCalendar(CalendarRequest request);
    List<CalendarFitterViewCandidateResponse> filterViewCandidateCalendars(CalendarFitterRequest request);
}
