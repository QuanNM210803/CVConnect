package com.cvconnect.dto.dashboard.admin;

import com.cvconnect.dto.career.CareerDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardJobAdByCareerDto {
    private CareerDto career;
    private Long numberOfJobAds;
    private Double avgSalary;
    private String avgSalaryStr;
}
