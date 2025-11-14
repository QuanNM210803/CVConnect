package com.cvconnect.service.impl;

import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;
import com.cvconnect.entity.SearchHistoryOutside;
import com.cvconnect.repository.SearchHistoryOutsideRepository;
import com.cvconnect.service.SearchHistoryOutsideService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryOutsideServiceImpl implements SearchHistoryOutsideService {
    @Autowired
    private SearchHistoryOutsideRepository searchHistoryOutsideRepository;

    @Override
    public List<SearchHistoryOutsideDto> findByUserId(Long userId, Long limit) {
        List<SearchHistoryOutside> histories = searchHistoryOutsideRepository.findByUserIdLimit(userId, limit);
        if(histories.isEmpty()){
            return List.of();
        }
        return ObjectMapperUtils.convertToList(histories, SearchHistoryOutsideDto.class);
    }

    @Override
    public void create(SearchHistoryOutsideDto dto) {
        SearchHistoryOutside entity = ObjectMapperUtils.convertToObject(dto, SearchHistoryOutside.class);
        searchHistoryOutsideRepository.save(entity);
    }
}
