package com.cvconnect.dto.auth;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgAddressRequest {
    private boolean isHeadquarter;

    @NotBlank(message = Messages.PROVINCE_REQUIRE)
    private String province;

    private String district;
    private String ward;

    @NotBlank(message = Messages.DETAIL_ADDRESS_REQUIRE)
    private String detailAddress;
}
