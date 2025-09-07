package com.cvconnect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachFileDto extends BaseDto<Instant> {
    private String originalFilename;

    private String baseFilename;

    private String extension;

    private String filename;

    private String format;

    private String resourceType;

    private String secureUrl;

    private String type;

    private String url;

    private String publicId;

    private String folder;
}