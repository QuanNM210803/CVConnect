package com.cvconnect.dto.calendar;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarDetailInViewGeneralRequest {
    @NotNull(message = Messages.CALENDAR_NOT_FOUND)
    private Long calendarId;
    private Long candidateInfoIds;
}
