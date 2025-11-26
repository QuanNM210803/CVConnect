package com.cvconnect.dto.career;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.Identifiable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareerRequest implements Identifiable {
    private Long id;
    @NotBlank(message = Messages.CAREER_CODE_REQUIRED)
    private String code;
    @NotBlank(message = Messages.CAREER_NAME_REQUIRED)
    private String name;
}
