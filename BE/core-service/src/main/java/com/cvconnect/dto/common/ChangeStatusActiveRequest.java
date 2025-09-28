package com.cvconnect.dto.common;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
public class ChangeStatusActiveRequest {
    @NotNull(message = Messages.IDS_REQUIRE)
    private List<Long> ids;
    @NotNull(message = Messages.STATUS_ACTIVE_REQUIRE)
    private Boolean active;
}
