package com.cvconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "candidates", schema = "cvconnect-user-service")
public class Candidate extends BaseEntity {
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

}