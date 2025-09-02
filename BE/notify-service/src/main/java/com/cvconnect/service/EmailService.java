package com.cvconnect.service;

import com.cvconnect.dto.SendEmailDto;

public interface EmailService {
    void sendEmail(SendEmailDto sendEmailDto);
    void resendEmail(SendEmailDto sendEmailDto, Long emailLogId);
}
