package com.cvconnect.dto.calendar;

import com.cvconnect.enums.ParticipationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarFitterRequest {
    private Instant dateFrom;
    private Instant dateTo;
    private ParticipationType participationType;
    private Long jobAdCandidateId;

    // man hinh lich trong ung vien chi cho loc theo participationType va jobAdCandidateId
}
