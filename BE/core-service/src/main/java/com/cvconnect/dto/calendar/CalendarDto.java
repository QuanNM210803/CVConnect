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
public class CalendarDto extends BaseDto<Instant> {
    private Long jobAdProcessId;

    private String calendarType;

    private Boolean joinSameTime;

    private LocalDate date;

    private LocalTime timeFrom;

    private Integer durationMinutes;

    private Long orgAddressId;

    private String meetingLink;

    private String note;

    private Long creatorId;
}
