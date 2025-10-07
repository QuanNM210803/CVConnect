package com.cvconnect.dto.industry;


import com.cvconnect.dto.industrySub.IndustrySubDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustryDto extends BaseDto<Instant> {
    private String code;
    private String name;
    private String description;

    // attribute expansion
    private List<IndustrySubDto> industrySubs;
}
