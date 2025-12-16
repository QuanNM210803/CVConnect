package com.cvconnect.entity;

import com.cvconnect.enums.ScheduleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_config", schema = "cvconnect-user-service")
public class JobConfig extends BaseEntity {
    @Size(max = 100)
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 50)
    private ScheduleType scheduleType;

    @Size(max = 100)
    @Column(name = "expression", nullable = false, length = 100)
    private String expression;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

}