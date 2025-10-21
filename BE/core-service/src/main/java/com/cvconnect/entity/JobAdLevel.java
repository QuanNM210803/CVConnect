package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_ad_level")
public class JobAdLevel extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

    @NotNull
    @Column(name = "level_id", nullable = false)
    private Long levelId;

}