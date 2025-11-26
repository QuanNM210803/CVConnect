package com.cvconnect.dto.level;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LevelRequest {
    private Long id;
    @NotBlank(message = Messages.LEVEL_CODE_REQUIRE)
    private String code;
    @NotBlank(message = Messages.LEVEL_NAME_REQUIRE)
    private String name;
}
