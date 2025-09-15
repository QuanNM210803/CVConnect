package com.cvconnect.dto.auth;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationRequest {
    @NotBlank(message = Messages.ORG_NAME_REQUIRE)
    private String name;
    private String description;
    private String website;
    private Integer staffCountFrom;
    private Integer staffCountTo;
    private boolean hasRemote;

    private List<Long> industryIds;
    @Valid
    private List<OrgAddressRequest> addresses;
}
