package com.cvconnect.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDto {
    private Long id;
    private String code;
    private String label;
    private String icon;
    private Long parentId;
    private Integer sortOrder;
    private String forMemberType;
}
