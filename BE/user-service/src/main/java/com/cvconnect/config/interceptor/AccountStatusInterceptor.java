package com.cvconnect.config.interceptor;

import com.cvconnect.dto.user.UserDto;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AccountStatusInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = WebUtils.getCurrentUserId();
        if(userId == null){
            return true;
        }
        UserDto userDto = userService.findById(userId);
        UserErrorCode errorCode = null;
        if(Boolean.FALSE.equals(userDto.getIsEmailVerified())){
            errorCode = UserErrorCode.EMAIL_NOT_VERIFIED;
        }
        if(Boolean.FALSE.equals(userDto.getIsActive())){
            errorCode = UserErrorCode.ACCOUNT_NOT_ACTIVE;
        }
        if(errorCode != null){
            Response<?> apiResponse = Response.builder()
                    .code(errorCode.getCode())
                    .message(localizationUtils.getLocalizedMessage(errorCode.getMessage()))
                    .build();
            response.setStatus(errorCode.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(ObjectMapperUtils.convertToJson(apiResponse));
            response.flushBuffer();
            return false;
        }
        return true;
    }
}
