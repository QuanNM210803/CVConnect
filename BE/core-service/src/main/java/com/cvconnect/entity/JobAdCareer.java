package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_ad_career")
public class JobAdCareer extends BaseEntity {
    @NotNull
    @Column(name = "career_id", nullable = false)
    private Long careerId;

    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

}