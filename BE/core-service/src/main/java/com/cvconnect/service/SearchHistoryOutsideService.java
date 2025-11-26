package com.cvconnect.service;

import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;

import java.util.List;

public interface SearchHistoryOutsideService {
    List<SearchHistoryOutsideDto> findByUserId(Long userId);
    void create(SearchHistoryOutsideDto dto);
    List<SearchHistoryOutsideDto> getMySearchHistoryOutside();
    void deleteByIds(List<Long> ids);
    void deleteAllByUserId();
}
