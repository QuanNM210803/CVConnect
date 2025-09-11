package com.cvconnect.dto.role;

import com.cvconnect.enums.MemberType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleFilterRequest extends FilterRequest {
    private String code;
    private String name;
    private List<MemberType> memberType;
    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant updatedAtStart;
    private Instant updatedAtEnd;
    private String createdBy;
    private String updatedBy;
}
