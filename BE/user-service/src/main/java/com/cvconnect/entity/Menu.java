package com.cvconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "menus", schema = "cvconnect-user-service")
public class Menu extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Size(max = 255)
    @Column(name = "icon")
    private String icon;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "for_member_type")
    private String forMemberType;

    @Column(name = "is_show")
    private Boolean isShow;
}