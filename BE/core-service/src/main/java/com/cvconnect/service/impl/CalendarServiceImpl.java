package com.cvconnect.service.impl;

import com.cvconnect.common.ReplacePlaceholder;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.calendar.CalendarCandidateInfoDto;
import com.cvconnect.dto.calendar.CalendarFitterRequest;
import com.cvconnect.dto.calendar.CalendarFitterViewCandidateResponse;
import com.cvconnect.dto.calendar.CalendarRequest;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.common.DataReplacePlaceholder;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.interviewPanel.InterviewPanelDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.entity.Calendar;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.enums.ProcessTypeEnum;
import com.cvconnect.repository.CalendarRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.SendEmailDto;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.service.SendEmailService;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private InterviewPanelService interviewPanelService;
    @Autowired
    private CalendarCandidateInfoService calendarCandidateInfoService;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private CandidateInfoApplyService candidateInfoApplyService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private ReplacePlaceholder replacePlaceholder;
    @Autowired
    private JobAdService jobAdService;

    @Override
    @Transactional
    public IDResponse<Long> createCalendar(CalendarRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long userId = WebUtils.getCurrentUserId();
        this.validateCreateCalendar(request, orgId);

        Calendar calendar = new Calendar();
        calendar.setJobAdProcessId(request.getJobAdProcessId());
        calendar.setCalendarType(request.getCalendarType().name());
        calendar.setJoinSameTime(request.isJoinSameTime());
        calendar.setDate(request.getDate());
        calendar.setTimeFrom(request.getTimeFrom());
        calendar.setDurationMinutes(request.getDurationMinutes());
        calendar.setOrgAddressId(request.getOrgAddressId());
        calendar.setMeetingLink(request.getMeetingLink());
        calendar.setNote(request.getNote());
        calendar.setCreatorId(userId);
        calendarRepository.save(calendar);

        // save interview panels
        List<InterviewPanelDto> interviewPanels = request.getParticipantIds().stream()
                .map(id -> {
                    InterviewPanelDto panel = new InterviewPanelDto();
                    panel.setCalendarId(calendar.getId());
                    panel.setInterviewerId(id);
                    return panel;
                }).toList();
        interviewPanelService.create(interviewPanels);

        // save calendar candidate info
        LocalDate date = request.getDate();
        LocalTime timeFrom = request.getTimeFrom();
        int durationMinutes = request.getDurationMinutes();
        boolean joinSameTime = request.isJoinSameTime();
        List<Long> candidateInfoIds = request.getCandidateInfoIds();

        List<CalendarCandidateInfoDto> calendarCandidates = new ArrayList<>();
        for (int i = 0; i < candidateInfoIds.size(); i++) {
            CalendarCandidateInfoDto calendarCandidate = new CalendarCandidateInfoDto();

            LocalDateTime startDateTime = LocalDateTime.of(date, timeFrom)
                    .plusMinutes(joinSameTime ? 0L : (long) i * durationMinutes);
            LocalDateTime endDateTime = startDateTime.plusMinutes(durationMinutes);

            calendarCandidate.setCalendarId(calendar.getId());
            calendarCandidate.setCandidateInfoId(candidateInfoIds.get(i));
            calendarCandidate.setDate(startDateTime.toLocalDate());
            calendarCandidate.setTimeFrom(startDateTime.toLocalTime());
            calendarCandidate.setTimeTo(endDateTime.toLocalTime());

            calendarCandidates.add(calendarCandidate);
        }
        calendarCandidateInfoService.create(calendarCandidates);

        // send email (candidate)
        Map<Long, CandidateInfoApplyDto> candidateInfos = candidateInfoApplyService.getByIds(candidateInfoIds);
        if (request.isSendEmail()) {
            String subject;
            String template;
            List<String> placeholders;

            // get email template
            Long emailTemplateId = request.getEmailTemplateId();
            if(emailTemplateId != null){
                EmailTemplateDto emailTemplateDto = restTemplateClient.getEmailTemplateById(emailTemplateId);
                if(ObjectUtils.isEmpty(emailTemplateDto)){
                    throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
                }
                subject = emailTemplateDto.getSubject();
                template = emailTemplateDto.getBody();
                placeholders = emailTemplateDto.getPlaceholderCodes();
            } else {
                CoreServiceUtils.validateManualEmail(request.getSubject(), request.getTemplate());
                subject = request.getSubject();
                template = request.getTemplate();
                placeholders = request.getPlaceholders();
            }

            JobAdDto jobAd = jobAdService.findByJobAdProcessId(request.getJobAdProcessId());
            UserDto userDto = restTemplateClient.getUser(userId);
            for(CalendarCandidateInfoDto calendarCandidate : calendarCandidates) {
                CandidateInfoApplyDto candidateInfo = candidateInfos.get(calendarCandidate.getCandidateInfoId());
                if(candidateInfo == null){
                    continue;
                }
                DataReplacePlaceholder dataReplacePlaceholder = DataReplacePlaceholder.builder()
                        .positionId(jobAd.getPositionId())
                        .jobAdName(jobAd.getTitle())
                        .jobAdProcessName(ProcessTypeEnum.APPLY.name())
                        .interviewLink(request.getMeetingLink())
                        .orgId(orgId)
                        .candidateName(candidateInfo.getFullName())
                        .hrName(userDto.getFullName())
                        .hrEmail(userDto.getEmail())
                        .hrPhone(userDto.getPhoneNumber())
                        .examStartTime(DateUtils.localDateTimeToInstant(LocalDateTime.of(calendarCandidate.getDate(), calendarCandidate.getTimeFrom())))
                        .examEndTime(DateUtils.localDateTimeToInstant(LocalDateTime.of(calendarCandidate.getDate(), calendarCandidate.getTimeTo())))
                        .examDuration(request.getDurationMinutes())
                        .locationId(request.getOrgAddressId())
                        .build();
                String body = replacePlaceholder.replacePlaceholder(template, placeholders, dataReplacePlaceholder);
                SendEmailDto sendEmailDto = SendEmailDto.builder()
                        .sender(userDto.getEmail())
                        .recipients(List.of(candidateInfo.getEmail()))
                        .subject(subject)
                        .body(body)
                        .candidateInfoId(candidateInfo.getId())
                        .jobAdId(jobAd.getId())
                        .orgId(orgId)
                        .emailTemplateId(emailTemplateId)
                        .build();
                sendEmailService.sendEmailWithBody(sendEmailDto);
            }
        }

        // todo: send notify (to interviewer)

        return IDResponse.<Long>builder()
                .id(calendar.getId())
                .build();
    }

    @Override
    public List<CalendarFitterViewCandidateResponse> filterViewCandidateCalendars(CalendarFitterRequest request) {
        return List.of();
    }

    private void validateCreateCalendar(CalendarRequest request, Long orgId) {
        // validate jobAdProcessId
        Boolean exists = jobAdProcessService.existByJobAdProcessIdAndOrgId(request.getJobAdProcessId(), orgId);
        if (!exists) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        // validate calendarType
        if(Objects.equals(request.getCalendarType().getType(), Constants.OFFLINE) && request.getOrgAddressId() == null){
            throw new AppException(CoreErrorCode.MEETING_ADDRESS_NOT_NULL);
        } else if (Objects.equals(request.getCalendarType().getType(), Constants.ONLINE) && ObjectUtils.isEmpty(request.getMeetingLink())) {
            throw new AppException(CoreErrorCode.MEETING_LINK_NOT_NULL);
        }

        // validate date
        if(request.getDate().isBefore(LocalDate.now()) || Objects.equals(request.getDate(), LocalDate.now())){
            throw new AppException(CoreErrorCode.DATE_BEFORE_TODAY);
        }
        if(request.getDurationMinutes() <= 0){
            throw new AppException(CoreErrorCode.DURATION_MINUTES_INVALID);
        }

        // validate participantIds
        Boolean isValidParticipants = restTemplateClient.checkOrgMember(request.getParticipantIds());
        if(!isValidParticipants) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        // validate candidateInfoIds
        Boolean isValidCandidateInfo = candidateInfoApplyService.validateCandidateInfoInProcess(request.getCandidateInfoIds(), request.getJobAdProcessId());
        if(!isValidCandidateInfo) {
            throw new AppException(CoreErrorCode.CANDIDATE_INFO_EXISTS_NOT_IN_PROCESS);
        }
    }
}
