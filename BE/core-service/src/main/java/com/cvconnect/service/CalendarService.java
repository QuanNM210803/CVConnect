package com.cvconnect.service;

import com.cvconnect.dto.calendar.CalendarRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface CalendarService {
    IDResponse<Long> createCalendar(CalendarRequest request);
}
