package com.cvconnect.dto.user;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.LocalDate;

public interface UserProjection extends BaseRepositoryDto {
    String getUsername();
    String getEmail();
    String getFullName();
    String getPhoneNumber();
    LocalDate getDateOfBirth();
    Boolean getIsEmailVerified();
    String getAccessMethod();
    Long getAvatarId();

}
