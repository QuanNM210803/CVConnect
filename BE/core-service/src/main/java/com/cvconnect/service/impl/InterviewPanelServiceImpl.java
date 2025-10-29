package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.interviewPanel.InterviewPanelDto;
import com.cvconnect.entity.InterviewPanel;
import com.cvconnect.repository.InterviewPanelRepository;
import com.cvconnect.service.InterviewPanelService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InterviewPanelServiceImpl implements InterviewPanelService {
    @Autowired
    private InterviewPanelRepository interviewPanelRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    public void create(List<InterviewPanelDto> dtos) {
        List<InterviewPanel> entities = ObjectMapperUtils.convertToList(dtos, InterviewPanel.class);
        interviewPanelRepository.saveAll(entities);
    }

    @Override
    public List<UserDto> getByCalendarId(Long calendarId) {
        List<InterviewPanel> interviewPanels = interviewPanelRepository.findByCalendarId(calendarId);
        List<Long> userIds = interviewPanels.stream()
                .map(InterviewPanel::getInterviewerId)
                .toList();
        if(userIds.isEmpty()) {
            return List.of();
        }
        Map<Long, UserDto> userMap = restTemplateClient.getUsersByIds(userIds);
        return userMap.values().stream()
                .toList();
    }
}
