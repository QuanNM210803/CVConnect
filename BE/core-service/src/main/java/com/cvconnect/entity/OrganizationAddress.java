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

@Getter
@Setter
@Entity
@Table(name = "organization_address")
public class OrganizationAddress extends BaseEntity {
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @ColumnDefault("false")
    @Column(name = "is_headquarter")
    private Boolean isHeadquarter;

    @Size(max = 150)
    @Column(name = "province", nullable = false, length = 150)
    private String province;

    @Size(max = 150)
    @Column(name = "district", length = 150)
    private String district;

    @Size(max = 150)
    @Column(name = "ward", length = 150)
    private String ward;

    @Size(max = 255)
    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

}