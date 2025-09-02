package com.cvconnect.service;

import com.cvconnect.dto.EmailLogDto;

import java.util.List;

public interface EmailLogService {
    Long save(EmailLogDto emailLogDto);
    EmailLogDto findById(Long id);
    List<EmailLogDto> getWaitResendEmail(Long limit);
}
