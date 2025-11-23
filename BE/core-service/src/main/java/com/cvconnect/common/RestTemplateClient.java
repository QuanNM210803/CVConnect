package com.cvconnect.common;

import com.cvconnect.dto.internal.response.ConversationDto;
import com.cvconnect.dto.internal.response.EmailConfigDto;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAdCandidate.MyConversationWithFilter;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RestTemplateClient {

    @Autowired
    private RestTemplateService restTemplateService;

    @Value("${server.user_service}")
    private String SERVER_USER_SERVICE;
    @Value("${server.notify_service}")
    private String SERVER_NOTIFY_SERVICE;

    public UserDto getUser(Long userId) {
        if (userId == null) return null;
        Response<UserDto> response = restTemplateService.getMethodRestTemplate(
                SERVER_USER_SERVICE + "/user/internal/get-by-id/{id}",
                new ParameterizedTypeReference<Response<UserDto>>() {},
                userId
        );
        return response.getData();
    }

    public EmailTemplateDto getEmailTemplateById(Long emailTemplateId) {
        Response<EmailTemplateDto> emailTemplate = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/email-template/internal/get-by-id/{id}",
                new ParameterizedTypeReference<Response<EmailTemplateDto>>() {},
                emailTemplateId
        );
        return emailTemplate.getData();
    }

    public Boolean checkOrgUserRole(Long userId, String roleCode, Long orgId) {
        Response<Boolean> response = restTemplateService.getMethodRestTemplate(
                SERVER_USER_SERVICE + "/user/internal/check-org-user-role/{userId}/{roleCode}/{orgId}",
                new ParameterizedTypeReference<Response<Boolean>>() {},
                userId,
                roleCode,
                orgId
        );
        return response.getData();
    }

    public List<EmailTemplateDto> getEmailTemplateByOrgId(Long orgId) {
        Response<List<EmailTemplateDto>> response = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/email-template/internal/get-by-org-id/{orgId}",
                new ParameterizedTypeReference<Response<List<EmailTemplateDto>>>() {},
                orgId
        );
        return response.getData();
    }

    public EmailConfigDto getEmailConfigByOrg(){
        Response<EmailConfigDto> response = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/email-config/internal/get-by-org",
                new ParameterizedTypeReference<Response<EmailConfigDto>>() {}
        );
        return response.getData();
    }

    public Long validOrgMember() {
        Response<Long> response = restTemplateService.getMethodRestTemplate(
                SERVER_USER_SERVICE + "/org-member/internal/valid-org-member",
                new ParameterizedTypeReference<Response<Long>>() {}
        );
        return response.getData();
    }

    public Map<Long,UserDto> getUsersByIds(List<Long> userIds) {
        Response<Map<Long,UserDto>> response = restTemplateService.postMethodRestTemplate(
                SERVER_USER_SERVICE + "/user/internal/get-by-ids",
                new ParameterizedTypeReference<Response<Map<Long,UserDto>>>() {},
                userIds
        );
        return response.getData();
    }

    public Boolean checkOrgMember(List<Long> userIds) {
        Response<Boolean> response = restTemplateService.postMethodRestTemplate(
                SERVER_USER_SERVICE + "/org-member/internal/check-org-member",
                new ParameterizedTypeReference<Response<Boolean>>() {},
                userIds
        );
        return response.getData();
    }

    public List<ConversationDto> getConversationUnread() {
        Response<List<ConversationDto>> response = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/conversation/internal/conversation-unread",
                new ParameterizedTypeReference<Response<List<ConversationDto>>>() {}
        );
        return response.getData();
    }

    public List<ConversationDto> getMyConversations() {
        Response<List<ConversationDto>> response = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/conversation/internal/my-conversations",
                new ParameterizedTypeReference<Response<List<ConversationDto>>>() {}
        );
        return response.getData();
    }

    public FilterResponse<ConversationDto> getMyConversationsWithFilter(MyConversationWithFilter filter) {
        Response<FilterResponse<ConversationDto>> response = restTemplateService.postMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/conversation/internal/my-conversations-filtered",
                new ParameterizedTypeReference<Response<FilterResponse<ConversationDto>>>() {},
                filter
        );
        return response.getData();
    }

}
