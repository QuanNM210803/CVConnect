package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.common.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustrySubRequest implements Identifiable {
    private Long id;
    @NotBlank(message = Messages.INDUSTRY_SUB_CODE_REQUIRED)
    private String code;
    @NotBlank(message = Messages.INDUSTRY_SUB_NAME_REQUIRED)
    private String name;
}
