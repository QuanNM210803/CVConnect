package com.cvconnect.dto.user;

import com.cvconnect.enums.MemberType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterRequest extends FilterRequest {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private List<MemberType> memberTypes;

}
