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
@Table(name = "placeholders", schema = "cvconnect-notify-service")
public class Placeholder extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 255)
    @Column(name = "member_type_used")
    private String memberTypeUsed;

}