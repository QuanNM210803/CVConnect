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
public class OrgAddressDto extends BaseDto<Instant> {
    private Long orgId;
    private Boolean isHeadquarter;
    private String province;
    private String district;
    private String ward;
    private String detailAddress;
}
