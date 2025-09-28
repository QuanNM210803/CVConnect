package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_ad_industry_sub")
public class JobAdIndustrySub extends BaseEntity {
    @NotNull
    @Column(name = "industry_sub_id", nullable = false)
    private Long industrySubId;

    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

}