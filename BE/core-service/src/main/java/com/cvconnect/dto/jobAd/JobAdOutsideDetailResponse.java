package com.cvconnect.dto.jobAd;

import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.enums.CurrencyTypeDto;
import com.cvconnect.dto.enums.JobAdStatusDto;
import com.cvconnect.dto.enums.JobTypeDto;
import com.cvconnect.dto.enums.SalaryTypeDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.position.PositionDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdOutsideDetailResponse {
    private Long id;
    private String title;
    private PositionDto position;
    private Instant dueDate;
    private Integer quantity;

    private JobTypeDto jobType;

    private SalaryTypeDto salaryType;
    private Integer salaryFrom;
    private Integer salaryTo;
    private CurrencyTypeDto currencyType;

    private Boolean isRemote;
    private List<OrgAddressDto> workLocations;
    private Boolean isAllLevel;
    private List<LevelDto> levels;

    private OrgDto org;

}
