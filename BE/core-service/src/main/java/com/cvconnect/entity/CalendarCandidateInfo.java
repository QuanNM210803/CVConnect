package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "calendar_candidate_info")
public class CalendarCandidateInfo extends BaseEntity {
    @NotNull
    @Column(name = "calendar_id", nullable = false)
    private Long calendarId;

    @NotNull
    @Column(name = "candidate_info_id", nullable = false)
    private Long candidateInfoId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "time_from", nullable = false)
    private LocalTime timeFrom;

    @NotNull
    @Column(name = "time_to", nullable = false)
    private LocalTime timeTo;

}