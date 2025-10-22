package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "candidate_summary_org")
public class CandidateSummaryOrg extends BaseEntity {
    @Column(name = "skill", length = Integer.MAX_VALUE)
    private String skill;

    @NotNull
    @Column(name = "level_id", nullable = false)
    private Long levelId;

    @NotNull
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @NotNull
    @Column(name = "candidate_info_id", nullable = false)
    private Long candidateInfoId;

}