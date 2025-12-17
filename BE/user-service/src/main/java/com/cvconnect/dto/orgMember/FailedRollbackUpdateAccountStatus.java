package com.cvconnect.dto.orgMember;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailedRollbackUpdateAccountStatus {
    private List<Long> orgIds;
    private Boolean active;
    private Instant updatedAt; // thoi gian cap nhat trang thai tai khoan cuoi cung
}
