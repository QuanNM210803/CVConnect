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
@Table(name = "role_menu", schema = "cvconnect-user-service")
public class RoleMenu extends BaseEntity {
    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Size(max = 255)
    @NotNull
    @Column(name = "permission", nullable = false)
    private String permission;

}