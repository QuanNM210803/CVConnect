package com.cvconnect.service;

import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;

import java.util.List;

public interface SearchHistoryOutsideService {
    List<SearchHistoryOutsideDto> findByUserId(Long userId, Long limit);
    void create(SearchHistoryOutsideDto dto);
}
