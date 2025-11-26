package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "organization_industry")
public class OrganizationIndustry extends BaseEntity {
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "industry_id", nullable = false)
    private Long industryId;

}