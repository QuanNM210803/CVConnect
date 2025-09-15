package com.cvconnect.dto.processType;

import com.cvconnect.constant.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessTypeRequest {
    private Long id;
    @NotBlank(message = Messages.PROCESS_TYPE_NAME_REQUIRE)
    private String code;
    @NotBlank(message = Messages.PROCESS_TYPE_CODE_REQUIRE)
    private String name;
    @NotNull(message = Messages.PROCESS_TYPE_SORT_ORDER_REQUIRE)
    private Integer sortOrder;
}
