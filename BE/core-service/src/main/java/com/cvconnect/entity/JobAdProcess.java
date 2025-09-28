package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "job_ad_process")
public class JobAdProcess extends BaseEntity {
    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @NotNull
    @Column(name = "job_ad_id", nullable = false)
    private Long jobAdId;

    @NotNull
    @Column(name = "process_type_id", nullable = false)
    private Long processTypeId;

}