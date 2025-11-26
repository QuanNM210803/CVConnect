package com.cvconnect.service.impl;

import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;
import com.cvconnect.entity.SearchHistoryOutside;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.SearchHistoryOutsideRepository;
import com.cvconnect.service.SearchHistoryOutsideService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryOutsideServiceImpl implements SearchHistoryOutsideService {
    @Autowired
    private SearchHistoryOutsideRepository searchHistoryOutsideRepository;

    @Override
    public List<SearchHistoryOutsideDto> findByUserId(Long userId) {
        List<SearchHistoryOutside> histories = searchHistoryOutsideRepository.findByUserId(userId);
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

    @Override
    public List<SearchHistoryOutsideDto>  getMySearchHistoryOutside() {
        Long userId = WebUtils.getCurrentUserId();
        if(userId == null){
            return List.of();
        }
        return this.findByUserId(userId).stream()
                .limit(5)
                .toList();
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        Long userId = WebUtils.getCurrentUserId();
        List<SearchHistoryOutside> searchHistoryOutsides = searchHistoryOutsideRepository.findByIds(ids, userId);
        if (searchHistoryOutsides.size() != ids.size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        searchHistoryOutsideRepository.deleteAll(searchHistoryOutsides);
    }

    @Override
    public void deleteAllByUserId() {
        Long userId = WebUtils.getCurrentUserId();
        searchHistoryOutsideRepository.deleteByUserId(userId);
    }
}
