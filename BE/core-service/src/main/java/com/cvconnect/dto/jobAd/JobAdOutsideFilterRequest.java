package com.cvconnect.dto.jobAd;

import com.cvconnect.enums.JobType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdOutsideFilterRequest extends FilterRequest {
    private String keyword; // title, keyword
    private List<Long> careerIds; // career
    private List<Long> levelIds; // level

    // location
    private String jobAdLocation;
    private Boolean isRemote;

    // salary
    private Long salaryFrom;
    private Long salaryTo;
    private Boolean negotiable;

    // job type
    private JobType jobType;

    private boolean isSearchOrg;
    private Long orgId;
}
