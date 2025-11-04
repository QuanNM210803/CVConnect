package com.cvconnect.dto.calendar;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.enums.CalendarTypeDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarFilterDetail {
    private Long calendarId;
    private List<CandidateInfoApplyDto> candidateInfos;
    private JobAdDto jobAd;
    private CalendarTypeDto calendarType;
    private LocalTime timeFrom;
    private LocalTime timeTo;
}
