package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.Identifiable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionProcessRequest implements Identifiable {
    private Long id;
    @NotNull(message = Messages.POSITION_PROCESS_NAME_REQUIRED)
    private String name;

    @NotNull(message = Messages.PROCESS_TYPE_REQUIRE)
    private Long processTypeId;

    @NotNull(message = Messages.SORT_ORDER_REQUIRE)
    private Integer sortOrder;
}
