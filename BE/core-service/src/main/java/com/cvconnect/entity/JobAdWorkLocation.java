package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_ad_work_location")
public class JobAdWorkLocation extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

    @NotNull
    @Column(name = "work_location_id", nullable = false)
    private Long workLocationId;

}