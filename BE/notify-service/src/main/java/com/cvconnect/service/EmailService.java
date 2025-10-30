package com.cvconnect.service;

import nmquan.commonlib.dto.SendEmailDto;

public interface EmailService {
    void sendEmail(SendEmailDto sendEmailDto);
    void resendEmail(SendEmailDto sendEmailDto, Long emailLogId);
    void resendEmailClient(Long emailLogId);
}
