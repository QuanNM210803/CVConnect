package com.cvconnect.entity;

import com.cvconnect.enums.MemberType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "cvconnect-user-service")
public class Role extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "member_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberType memberType;
}