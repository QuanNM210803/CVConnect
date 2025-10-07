package com.cvconnect.dto.industrySub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.Identifiable;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustrySubDto extends BaseDto<Instant> implements Identifiable {
    private String code;
    private String name;
    private Long industryId;
}
