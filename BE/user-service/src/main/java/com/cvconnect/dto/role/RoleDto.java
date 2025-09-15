package com.cvconnect.dto.role;

import com.cvconnect.dto.roleMenu.RoleMenuDto;
import com.cvconnect.enums.MemberType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto extends BaseDto<Instant> {
    private Long id;
    private String code;
    private String name;
    private MemberType memberType;
    private Boolean canUpdate;
    private Boolean canDelete;

    private MemberTypeDto memberTypeDto;
    List<RoleMenuDto> roleMenus;

    public RoleDto(Long id, String code, String name, MemberType memberType, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.memberType = memberType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;

    }
}
