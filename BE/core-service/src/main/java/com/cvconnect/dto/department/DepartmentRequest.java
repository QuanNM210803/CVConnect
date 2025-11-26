package com.cvconnect.dto.department;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentRequest {
    private Long id;
    @NotBlank(message = Messages.DEPARTMENT_CODE_REQUIRE)
    private String code;
    @NotBlank(message = Messages.DEPARTMENT_NAME_REQUIRE)
    private String name;
}
