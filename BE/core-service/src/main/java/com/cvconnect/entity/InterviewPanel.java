package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "interview_panel")
public class InterviewPanel extends BaseEntity {
    @NotNull
    @Column(name = "calendar_id", nullable = false)
    private Long calendarId;

    @NotNull
    @Column(name = "interviewer_id", nullable = false)
    private Long interviewerId;

}