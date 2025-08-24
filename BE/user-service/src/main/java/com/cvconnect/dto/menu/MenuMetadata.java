package com.cvconnect.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuMetadata {
    private Long id;
    private String menuCode;
    private String menuLabel;
    private String menuIcon;
    private String menuUrl;
    private Long parentId;
    private Integer menuSortOrder;
    List<String> permissions = new ArrayList<>();
    private List<MenuMetadata> children = new ArrayList<>();
}
