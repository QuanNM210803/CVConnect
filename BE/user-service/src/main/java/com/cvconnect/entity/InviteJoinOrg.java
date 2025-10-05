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
@Table(name = "invite_join_org", schema = "cvconnect-user-service")
public class InviteJoinOrg extends BaseEntity {
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Size(max = 100)
    @NotNull
    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

}