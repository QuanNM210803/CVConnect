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
@Table(name = "industry")
public class Industry extends BaseEntity {
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}