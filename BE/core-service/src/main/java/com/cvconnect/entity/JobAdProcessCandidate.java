package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_ad_process_candidate")
public class JobAdProcessCandidate extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_process_id", nullable = false)
    private Long jobAdProcessId;

    @NotNull
    @Column(name = "job_ad_candidate_id", nullable = false)
    private Long jobAdCandidateId;

    @Column(name = "action_date")
    private Instant actionDate;

    @ColumnDefault("false")
    @Column(name = "is_current_process")
    private Boolean isCurrentProcess;

}