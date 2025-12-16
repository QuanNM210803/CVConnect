package com.cvconnect.common;

import com.cvconnect.dto.failedRollback.FailedRollbackOrgCreation;
import com.cvconnect.dto.internal.response.AttachFileDto;
import com.cvconnect.dto.auth.OrganizationRequest;
import com.cvconnect.dto.internal.response.OrgDto;
import nmquan.commonlib.dto.request.ObjectAndFileRequest;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class RestTemplateClient {
    @Autowired
    private RestTemplateService restTemplateService;

    @Value("${server.core_service}")
    private String SERVER_CORE_SERVICE;
    @Value("${server.notify_service}")
    private String SERVER_NOTIFY_SERVICE;


    public OrgDto getOrgById(Long orgId) {
        Response<OrgDto> orgDtoResponse = restTemplateService.getMethodRestTemplate(
                SERVER_CORE_SERVICE + "/org/internal/get-by-id/{orgId}",
                new ParameterizedTypeReference<Response<OrgDto>>() {},
                orgId
        );
        return orgDtoResponse.getData();
    }

    public AttachFileDto getAttachFileById(Long attachFileId) {
        Response<AttachFileDto> response = restTemplateService.getMethodRestTemplate(
                SERVER_CORE_SERVICE + "/attach-file/internal/get-by-id/{avatarId}",
                new ParameterizedTypeReference<Response<AttachFileDto>>() {},
                attachFileId
        );
        return response.getData();
    }

    public IDResponse<Long> createOrg(ObjectAndFileRequest<OrganizationRequest> request){
        Response<IDResponse<Long>> response = restTemplateService.uploadFilesWithObject(
                SERVER_CORE_SERVICE + "/org/internal/create",
                new ParameterizedTypeReference<Response<IDResponse<Long>>>() {},
                request
        );
        return response.getData();
    }

    public List<Long> uploadFile(MultipartFile[] files) {
        Response<List<Long>> response = restTemplateService.uploadFiles(
                SERVER_CORE_SERVICE + "/attach-file/internal/uploads",
                new ParameterizedTypeReference<Response<List<Long>>>() {},
                files
        );
        return response.getData();
    }

    public void deleteAttachFilesByIds(List<Long> ids) {
        restTemplateService.postMethodRestTemplate(
                SERVER_CORE_SERVICE + "/attach-file/internal/delete-by-ids",
                new ParameterizedTypeReference<Response<Void>>() {},
                ids
        );
    }

    // rollback org creation
    public void deleteOrg(FailedRollbackOrgCreation payload) {
        restTemplateService.postMethodRestTemplate(
                SERVER_CORE_SERVICE + "/org/internal/delete",
                new ParameterizedTypeReference<Response<Void>>() {},
                payload
        );
    }

}
