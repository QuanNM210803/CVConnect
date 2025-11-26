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
@Table(name = "position_process")
public class PositionProcess extends BaseEntity {
    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @NotNull
    @Column(name = "process_type_id", nullable = false)
    private Long processTypeId;

    @NotNull
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

}