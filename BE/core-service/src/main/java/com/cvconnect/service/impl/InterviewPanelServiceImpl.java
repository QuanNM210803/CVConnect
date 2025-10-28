package com.cvconnect.service.impl;

import com.cvconnect.dto.interviewPanel.InterviewPanelDto;
import com.cvconnect.entity.InterviewPanel;
import com.cvconnect.repository.InterviewPanelRepository;
import com.cvconnect.service.InterviewPanelService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewPanelServiceImpl implements InterviewPanelService {
    @Autowired
    private InterviewPanelRepository interviewPanelRepository;

    @Override
    public void create(List<InterviewPanelDto> dtos) {
        List<InterviewPanel> entities = ObjectMapperUtils.convertToList(dtos, InterviewPanel.class);
        interviewPanelRepository.saveAll(entities);
    }
}
