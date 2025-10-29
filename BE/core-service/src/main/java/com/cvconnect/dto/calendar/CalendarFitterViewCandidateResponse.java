package com.cvconnect.dto.calendar;

import com.cvconnect.dto.enums.CalendarTypeDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarFitterViewCandidateResponse {
    private Long id;
    private String labelDate;
    private Long numOfCalendars;
    private JobAdProcessDto jobAdProcess;
    private UserDto creator;
    private CalendarTypeDto calendarType;
    private LocalTime timeTo;
    private LocalTime timeFrom;
}
