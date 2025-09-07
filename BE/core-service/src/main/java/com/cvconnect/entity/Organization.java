package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "organization")
public class Organization extends BaseEntity {
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "logo_id")
    private Long logoId;

    @Column(name = "cover_photo_id")
    private Long coverPhotoId;

    @Size(max = 255)
    @Column(name = "website")
    private String website;

    @Column(name = "staff_count_from")
    private Integer staffCountFrom;

    @Column(name = "staff_count_to")
    private Integer staffCountTo;

}