package com.cvconnect.controller;

import com.cvconnect.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;

}
