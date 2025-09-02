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
@Table(name = "email_config", schema = "cvconnect-notify-service")
public class EmailConfig extends BaseEntity {
    @Size(max = 255)
    @NotNull
    @Column(name = "host", nullable = false)
    private String host;

    @NotNull
    @Column(name = "port", nullable = false)
    private Integer port;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("0")
    @Column(name = "is_ssl")
    private Boolean isSsl;

    @Size(max = 50)
    @ColumnDefault("'smtp'")
    @Column(name = "protocol", length = 50)
    private String protocol;

    @Column(name = "org_id")
    private Long orgId;

}