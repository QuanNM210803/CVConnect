package com.cvconnect.dto.menu;

public interface MenuProjection {
    Long getId();
    String getMenuCode();
    String getMenuLabel();
    String getMenuIcon();
    String getMenuUrl();
    Long getParentId();
    Integer getMenuSortOrder();
    String getPermission();
}
