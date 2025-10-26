package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "candidate_evaluation")
public class CandidateEvaluation extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_process_candidate_id", nullable = false)
    private Long jobAdProcessCandidateId;

    @NotNull
    @Column(name = "evaluator_id", nullable = false)
    private Long evaluatorId;

    @NotNull
    @Column(name = "comments", nullable = false, length = Integer.MAX_VALUE)
    private String comments;

    @Column(name = "score")
    private BigDecimal score;

}