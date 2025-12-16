package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "failed_rollback", schema = "cvconnect-user-service")
public class FailedRollback extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @NotNull
    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @ColumnDefault("0")
    @Column(name = "status")
    private Boolean status;

    @ColumnDefault("0")
    @Column(name = "retry_count")
    private Integer retryCount;

}