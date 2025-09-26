package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionLevelRequest {
    @NotBlank(message = Messages.POSITION_LEVEL_NAME_REQUIRED)
    private String name;

    @NotNull(message = Messages.LEVEL_REQUIRE)
    private Long levelId;
}
