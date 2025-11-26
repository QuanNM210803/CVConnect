package com.cvconnect.dto.jobAd;

import com.cvconnect.dto.org.WorkingLocationDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdOutsideFilterResponse<T> {
    private FilterResponse<T> data;
    private List<WorkingLocationDto> locations;
}
