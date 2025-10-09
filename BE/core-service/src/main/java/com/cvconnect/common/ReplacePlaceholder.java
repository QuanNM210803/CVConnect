package com.cvconnect.common;

import com.cvconnect.dto.DataReplacePlaceholder;
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
            String value = getValueForPlaceholder(placeholder, baseData);
            if (value != null) {
                body = body.replace(placeholder, value);
            }
        }
        return body;
    }

    public String previewEmailDefault(String template, List<String> placeholders){
        if (template == null || placeholders == null) {
            return template;
        }
        String body = template;
        for (String placeholder : placeholders) {
            String value = getValueForPlaceholder(placeholder, null);
            if (value != null) {
                body = body.replace(placeholder, value);
            }
        }
        return body;
    }

    private String getValueForPlaceholder(String placeholder, DataReplacePlaceholder baseData) {
        switch (placeholder) {
            case "${jobPosition}":
                if(baseData == null) {
                    return "Kỹ sư phát triển phần mềm";
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
                if(baseData == null) {
                    return "Lập trình viên Java";
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
                if(baseData == null) {
                    return "Ứng tuyển";
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
            case "${interviewLink}":
                if(baseData == null) {
                    return "https://meet.google.com/";
                }
                String link = baseData.getInterviewLink();
                return link != null ? link : DOT_DOT_DOT;
            case "${orgAddress}":
                if(baseData == null) {
                    return "36A, Dich Vong Hau, Ha Noi";
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
                if(baseData == null) {
                    return "Nguyễn Minh Quân";
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
                if(baseData == null) {
                    return "Công ty Cổ phần ABC";
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
                if(baseData == null) {
                    return "Nguyễn Văn A";
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
                if(baseData == null) {
                    return "0901234567";
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
                if(baseData == null) {
                    return "test1@gmail.com";
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
                if(baseData == null) {
                    return "01/12/2025";
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
                if(baseData == null) {
                    return "08:00";
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
                if(baseData == null) {
                    return "10:00";
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
                if(baseData == null) {
                    return "120";
                }
                Integer examDuration = baseData.getExamDuration();
                return examDuration != null ? examDuration.toString() : DOT_DOT_DOT;
            case "${interview-examLocation}":
                if(baseData == null) {
                    return "36A, Dich Vong Hau, Ha Noi";
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
