package com.cvconnect.dto.inviteJoinOrg;

import com.cvconnect.constant.Messages;
import com.cvconnect.enums.InviteJoinStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyInviteUserRequest {
    @NotBlank(message = Messages.INVITE_NOT_FOUND)
    private String token;
    @NotNull(message = Messages.STATUS_NOT_BLANK)
    private InviteJoinStatus status;
}
