package com.cvconnect.common;

import com.cvconnect.dto.common.DataReplacePlaceholder;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.service.*;
import nmquan.commonlib.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReplacePlaceholder {
    @Autowired
    private PositionService positionService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private JobAdService jobAdService;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private OrgAddressService orgAddressService;
    @Autowired
    private CandidateInfoApplyService candidateInfoApplyService;
    @Autowired
    private RestTemplateClient restTemplateClient;

    private static final String DOT_DOT_DOT = "...";

    public String replacePlaceholder(String template, List<String> placeholders, DataReplacePlaceholder baseData) {
        if (template == null || placeholders == null || baseData == null) {
            return template;
        }
        String body = template;
        for (String placeholder : placeholders) {
            String value = getValueForPlaceholder(placeholder, baseData, false);
            if (value != null) {
                body = body.replace(placeholder, value);
            }
        }
        return body;
    }

    public String previewEmail(String template, List<String> placeholders, DataReplacePlaceholder baseData, Boolean isDefault) {
        if (template == null || placeholders == null) {
            return template;
        }
        String body = template;
        for (String placeholder : placeholders) {
            String value = getValueForPlaceholder(placeholder, baseData, isDefault);
            if (value != null) {
                body = body.replace(placeholder, value);
            }
        }
        return body;
    }

    private String getValueForPlaceholder(String placeholder, DataReplacePlaceholder baseData, Boolean isDefault) {
        switch (placeholder) {
            case "${jobPosition}":
                if(isDefault){
                    return "Kỹ sư phát triển phần mềm";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getPositionName() != null){
                    return baseData.getPositionName();
                }
                if(baseData.getPositionId() == null){
                    return DOT_DOT_DOT;
                }
                PositionDto positionDto = positionService.findById(baseData.getPositionId());
                return positionDto != null && positionDto.getName() != null
                        ? positionDto.getName()
                        : DOT_DOT_DOT;
            case "${postTitle}":
                if(isDefault){
                    return "Lập trình viên Java";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getJobAdName() != null){
                    return baseData.getJobAdName();
                }
                if(baseData.getJobAdId() == null){
                    return DOT_DOT_DOT;
                }
                JobAdDto jobAdDto = jobAdService.findById(baseData.getJobAdId());
                return jobAdDto != null && jobAdDto.getTitle() != null
                        ? jobAdDto.getTitle()
                        : DOT_DOT_DOT;
            case "${currentRound}":
                if (isDefault){
                    return "Ứng tuyển";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getJobAdProcessName() != null){
                    return baseData.getJobAdProcessName();
                }
                if(baseData.getJobAdProcessId() == null){
                    return DOT_DOT_DOT;
                }
                JobAdProcessDto jobAdProcessDto = jobAdProcessService.getById(baseData.getJobAdProcessId());
                return jobAdProcessDto != null && jobAdProcessDto.getName() != null
                        ? jobAdProcessDto.getName()
                        : DOT_DOT_DOT;
            case "${nextRound}":
                if(isDefault){
                    return "Phỏng vấn kỹ thuật";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getNextJobAdProcessName() != null){
                    return baseData.getNextJobAdProcessName();
                }
                if(baseData.getNextJobAdProcessId() == null){
                    return DOT_DOT_DOT;
                }
                JobAdProcessDto nextJobAdProcessDto = jobAdProcessService.getById(baseData.getNextJobAdProcessId());
                return nextJobAdProcessDto != null && nextJobAdProcessDto.getName() != null
                        ? nextJobAdProcessDto.getName()
                        : DOT_DOT_DOT;
            case "${interviewLink}":
                if(isDefault){
                    return "https://meet.google.com/";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                String link = baseData.getInterviewLink();
                return link != null ? link : DOT_DOT_DOT;
            case "${orgAddress}":
                if(isDefault){
                    return "36A, Dich Vong Hau, Ha Noi";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getOrgAddress() != null){
                    return baseData.getOrgAddress();
                }
                if(baseData.getOrgAddressId() == null){
                    return DOT_DOT_DOT;
                }
                OrgAddressDto orgAddressDtoForAddress = orgAddressService.getById(baseData.getOrgAddressId());
                return orgAddressDtoForAddress != null && orgAddressDtoForAddress.getDisplayAddress() != null
                        ? orgAddressDtoForAddress.getDisplayAddress()
                        : DOT_DOT_DOT;
            case "${candidateName}":
                if(isDefault){
                    return "Nguyễn Minh Quân";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getCandidateName() != null){
                    return baseData.getCandidateName();
                }
                if(baseData.getCandidateInfoApplyId() == null){
                    return DOT_DOT_DOT;
                }
                CandidateInfoApplyDto candidateInfoApplyDto = candidateInfoApplyService.getById(baseData.getCandidateInfoApplyId());
                return candidateInfoApplyDto != null && candidateInfoApplyDto.getFullName() != null
                        ? candidateInfoApplyDto.getFullName()
                        : DOT_DOT_DOT;
            case "${salutation}":
                return "Anh/Chị";
            case "${orgName}":
                if(isDefault){
                    return "Công ty Cổ phần ABC";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getOrgName() != null){
                    return baseData.getOrgName();
                }
                if(baseData.getOrgId() == null){
                    return DOT_DOT_DOT;
                }
                OrgDto orgDto = orgService.findById(baseData.getOrgId());
                return orgDto != null && orgDto.getName() != null
                        ? orgDto.getName()
                        : DOT_DOT_DOT;
            case "${hrName}":
                if(isDefault){
                    return "Nguyễn Văn A";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getHrName() != null){
                    return baseData.getHrName();
                }
                if(baseData.getHrContactId() == null){
                    return DOT_DOT_DOT;
                }
                UserDto hrContactForName = restTemplateClient.getUser(baseData.getHrContactId());
                return hrContactForName != null && hrContactForName.getFullName() != null
                        ? hrContactForName.getFullName()
                        : DOT_DOT_DOT;
            case "${hrPhone}":
                if(isDefault){
                    return "0901234567";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getHrPhone() != null){
                    return baseData.getHrPhone();
                }
                if(baseData.getHrContactId() == null){
                    return DOT_DOT_DOT;
                }
                UserDto hrContactForPhone = restTemplateClient.getUser(baseData.getHrContactId());
                return hrContactForPhone != null && hrContactForPhone.getPhoneNumber() != null
                        ? hrContactForPhone.getPhoneNumber()
                        : DOT_DOT_DOT;
            case "${hrEmail}":
                if(isDefault){
                    return "test@gmail.com";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if(baseData.getHrEmail() != null){
                    return baseData.getHrEmail();
                }
                if(baseData.getHrContactId() == null){
                    return DOT_DOT_DOT;
                }
                UserDto hrContactForEmail = restTemplateClient.getUser(baseData.getHrContactId());
                return hrContactForEmail != null && hrContactForEmail.getEmail() != null
                        ? hrContactForEmail.getEmail()
                        : DOT_DOT_DOT;
            case "${examDate}":
                if(isDefault){
                    return "01/12/2025";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                Instant date = baseData.getExamStartTime();
                if(date != null){
                    LocalDateTime start = LocalDateTime.ofInstant(date, CommonConstants.ZONE.HCM);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(CommonConstants.DATE_TIME.DD_MM_YYYY);
                    return start.format(dateFormatter);
                } else {
                    return DOT_DOT_DOT;
                }
            case "${startTime}":
                if(isDefault){
                    return "08:00";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                Instant startTime = baseData.getExamStartTime();
                if(startTime != null){
                    LocalDateTime start = LocalDateTime.ofInstant(startTime, CommonConstants.ZONE.HCM);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(CommonConstants.DATE_TIME.HH_MM);
                    return start.format(dateFormatter);
                } else {
                    return DOT_DOT_DOT;
                }
            case "${endTime}":
                if(isDefault){
                    return "10:00";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                Instant endTime = baseData.getExamEndTime();
                if(endTime != null){
                    LocalDateTime end = LocalDateTime.ofInstant(endTime, CommonConstants.ZONE.HCM);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(CommonConstants.DATE_TIME.HH_MM);
                    return end.format(dateFormatter);
                } else {
                    return DOT_DOT_DOT;
                }
            case "${examDuration}":
                if(isDefault){
                    return "120";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                Integer examDuration = baseData.getExamDuration();
                return examDuration != null ? examDuration.toString() : DOT_DOT_DOT;
            case "${interview-examLocation}":
                if(isDefault){
                    return "36A, Dich Vong Hau, Ha Noi";
                }
                if(baseData == null) {
                    return DOT_DOT_DOT;
                }
                if (baseData.getLocationName() != null) {
                    return baseData.getLocationName();
                }
                if (baseData.getLocationId() == null) {
                    return DOT_DOT_DOT;
                }
                OrgAddressDto orgAddressDto = orgAddressService.getById(baseData.getLocationId());
                return orgAddressDto != null && orgAddressDto.getDetailAddress() != null
                        ? orgAddressDto.getDetailAddress()
                        : DOT_DOT_DOT;
            default:
                return null;
        }
    }

}
