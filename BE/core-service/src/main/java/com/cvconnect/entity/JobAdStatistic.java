package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "job_ad_statistic")
public class JobAdStatistic extends BaseEntity {
    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Long viewCount;

}