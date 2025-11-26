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
@Table(name = "attach_file")
public class AttachFile extends BaseEntity {
    @Size(max = 255)
    @NotNull
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Size(max = 255)
    @NotNull
    @Column(name = "base_filename", nullable = false)
    private String baseFilename;

    @Size(max = 50)
    @NotNull
    @Column(name = "extension", nullable = false, length = 50)
    private String extension;

    @Size(max = 255)
    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @Size(max = 100)
    @Column(name = "format", length = 100)
    private String format;

    @Size(max = 100)
    @NotNull
    @Column(name = "resource_type", nullable = false, length = 100)
    private String resourceType;

    @Size(max = 500)
    @NotNull
    @Column(name = "secure_url", nullable = false, length = 500)
    private String secureUrl;

    @Size(max = 100)
    @NotNull
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Size(max = 500)
    @NotNull
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Size(max = 255)
    @NotNull
    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Size(max = 255)
    @Column(name = "folder")
    private String folder;

}