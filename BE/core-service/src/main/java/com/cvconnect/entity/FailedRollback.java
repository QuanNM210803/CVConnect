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
@Table(name = "failed_rollback")
@AttributeOverrides({
        @AttributeOverride(name = "isActive", column = @Column(name = "is_active")),
        @AttributeOverride(name = "isDeleted", column = @Column(name = "is_deleted")),
        @AttributeOverride(name = "createdBy", column = @Column(name = "created_by", length = 100)),
        @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by", length = 100))
})
public class FailedRollback extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @NotNull
    @Column(name = "payload", nullable = false, length = Integer.MAX_VALUE)
    private String payload;

    @Column(name = "error_message", length = Integer.MAX_VALUE)
    private String errorMessage;

    @ColumnDefault("false")
    @Column(name = "status")
    private Boolean status;

    @ColumnDefault("0")
    @Column(name = "retry_count")
    private Integer retryCount;

}