package com.cvconnect.controller;

import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgAddressRequest;
import com.cvconnect.service.OrgAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org-address")
public class OrgAddressController {
    @Autowired
    private OrgAddressService orgAddressService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/get-all")
    @Operation(summary = "Get all organization addresses")
    @PreAuthorize("hasAnyAuthority('ORGANIZATION', 'ORG_ADDRESS:VIEW')")
    public ResponseEntity<Response<List<OrgAddressDto>>> getAllOrgAddresses() {
        return ResponseUtils.success(orgAddressService.getAll());
    }

    @PostMapping("/save")
    @Operation(summary = "Save organization addresses")
    @PreAuthorize("hasAnyAuthority('ORG_ADDRESS:ADD', 'ORG_ADDRESS:UPDATE', 'ORG_ADDRESS:DELETE')")
    public ResponseEntity<Response<Void>> save(@Valid @RequestBody List<OrgAddressRequest> requests) {
        orgAddressService.save(requests);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }
}
