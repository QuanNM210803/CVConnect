package com.cvconnect.dto.calendar;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.enums.CalendarTypeDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarDetail {
    private JobAdDto jobAd;
    private JobAdProcessDto jobAdProcess;
    private UserDto creator;
    private CalendarTypeDto calendarType;
    private LocalDate date;
    private LocalTime timeTo;
    private LocalTime timeFrom;
    private OrgAddressDto location;
    private String meetingLink;
    private List<CandidateInfoApplyDto> candidates;
    private List<UserDto> participants;
}
