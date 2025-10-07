package com.cvconnect.dto.positionLevel;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nmquan.commonlib.dto.Identifiable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionLevelRequest implements Identifiable {
    private Long id;
    @NotBlank(message = Messages.POSITION_LEVEL_NAME_REQUIRED)
    private String name;

    @NotNull(message = Messages.LEVEL_REQUIRE)
    private Long levelId;
}
