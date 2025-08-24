package com.cvconnect.controller;

import com.cvconnect.dto.menu.MenuMetadata;
import com.cvconnect.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menu-by-role/{roleId}")
    @Operation(summary = "Get menus by role ID")
    public ResponseEntity<Response<List<MenuMetadata>>> getMenusByRoleId(@PathVariable Long roleId) {
        return ResponseUtils.success(menuService.getMenusByRoleId(roleId));
    }
}
