package com.cvconnect.controller;

import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.service.OrgAddressService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/org-address")
public class OrgAddressController {
    @Autowired
    private OrgAddressService orgAddressService;

    @GetMapping("/get-all")
    @Operation(summary = "Get all organization addresses")
    public ResponseEntity<Response<List<OrgAddressDto>>> getAllOrgAddresses() {
        return ResponseUtils.success(orgAddressService.getAll());
    }
}
