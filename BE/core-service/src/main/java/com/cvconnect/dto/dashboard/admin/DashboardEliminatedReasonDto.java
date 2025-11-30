package com.cvconnect.dto.dashboard.admin;

import com.cvconnect.dto.enums.EliminateReasonEnumDto;
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
public class DashboardEliminatedReasonDto {
    private EliminateReasonEnumDto eliminateReason;
    private Long numberOfEliminated;
    private Double percent;
}
