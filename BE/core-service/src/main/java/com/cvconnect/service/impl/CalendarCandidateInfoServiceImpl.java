package com.cvconnect.service.impl;

import com.cvconnect.dto.calendar.CalendarCandidateInfoDto;
import com.cvconnect.entity.CalendarCandidateInfo;
import com.cvconnect.repository.CalendarCandidateInfoRepository;
import com.cvconnect.service.CalendarCandidateInfoService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarCandidateInfoServiceImpl implements CalendarCandidateInfoService {
    @Autowired
    private CalendarCandidateInfoRepository calendarCandidateInfoRepository;

    @Override
    public void create(List<CalendarCandidateInfoDto> dtos) {
        List<CalendarCandidateInfo> entities = ObjectMapperUtils.convertToList(dtos, CalendarCandidateInfo.class);
        calendarCandidateInfoRepository.saveAll(entities);
    }

    @Override
    public CalendarCandidateInfoDto getByCalendarIdAndCandidateInfoId(Long calendarId, Long candidateInfoId) {
        CalendarCandidateInfo entity = calendarCandidateInfoRepository.findByCalendarIdAndCandidateInfoId(calendarId, candidateInfoId);
        if (entity == null) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(entity, CalendarCandidateInfoDto.class);
    }
}
