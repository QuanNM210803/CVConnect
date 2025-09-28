package com.cvconnect.dto.industry;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.IndustrySubRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustryRequest {
    private Long id;
    @NotBlank(message = Messages.INDUSTRY_CODE_REQUIRED)
    private String code;
    @NotBlank(message = Messages.INDUSTRY_NAME_REQUIRED)
    private String name;
    private String description;

    @Valid
    private List<IndustrySubRequest> industrySubs;
}
