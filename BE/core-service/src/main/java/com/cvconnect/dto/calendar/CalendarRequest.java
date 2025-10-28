package com.cvconnect.dto.calendar;

import com.cvconnect.constant.Messages;
import com.cvconnect.enums.CalendarType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarRequest {
    @NotNull(message = Messages.JOB_AD_NOT_FOUND)
    private Long jobAdProcessId;
    @NotNull(message = Messages.CALENDAR_TYPE_NOT_NULL)
    private CalendarType calendarType;
    private boolean joinSameTime;
    @NotNull(message = Messages.DATE_NOT_NULL)
    private LocalDate date;
    @NotNull(message = Messages.TIME_FROM_NOT_NULL)
    private LocalTime timeFrom;
    @NotNull(message = Messages.DURATION_MINUTES_NOT_NULL)
    private Integer durationMinutes;

    private Long orgAddressId;
    private String meetingLink;

    private String note;
    @NotNull(message = Messages.PARTICIPANT_IDS_NOT_NULL)
    private List<Long> participantIds;
    @NotNull(message = Messages.CANDIDATE_INFO_IDS_NOT_NULL)
    private List<Long> candidateInfoIds;

    private boolean isSendEmail;
    private Long emailTemplateId;

    private String subject;
    private String template;
    private List<String> placeholders;
}
