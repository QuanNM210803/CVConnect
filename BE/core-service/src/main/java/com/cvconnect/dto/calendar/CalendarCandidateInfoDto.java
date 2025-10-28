package com.cvconnect.dto.calendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarCandidateInfoDto extends BaseDto<Instant> {
    private Long calendarId;

    private Long candidateInfoId;

    private LocalDate date;

    private LocalTime timeFrom;

    private LocalTime timeTo;
}
