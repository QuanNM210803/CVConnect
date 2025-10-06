package com.cvconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_ad_candidate")
public class JobAdCandidate extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

    @NotNull
    @Column(name = "candidate_info_id", nullable = false)
    private Long candidateInfoId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "apply_date")
    private Instant applyDate;

    @Size(max = 100)
    @NotNull
    @Column(name = "candidate_status", nullable = false, length = 100)
    private String candidateStatus;

    @Column(name = "eliminate_reason_type", length = Integer.MAX_VALUE)
    private String eliminateReasonType;

    @Column(name = "eliminate_reason_detail", length = Integer.MAX_VALUE)
    private String eliminateReasonDetail;

    @Column(name = "onboard_date")
    private Instant onboardDate;

}