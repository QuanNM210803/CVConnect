package com.cvconnect.dto.org;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgDto extends BaseDto<Instant> {
    private String name;
    private String description;
    private Long logoId;
    private Long coverPhotoId;
    private String website;
    private Integer staffCountFrom;
    private Integer staffCountTo;
}
