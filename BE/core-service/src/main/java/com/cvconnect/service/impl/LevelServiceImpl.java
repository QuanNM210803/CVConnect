package com.cvconnect.service.impl;

import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.level.LevelFilterRequest;
import com.cvconnect.dto.level.LevelRequest;
import com.cvconnect.entity.Level;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.LevelRepository;
import com.cvconnect.service.LevelService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    private LevelRepository levelRepository;

    @Override
    public LevelDto getById(Long id) {
        Level level = levelRepository.findById(id).orElse(null);
        if (level == null) return null;
        return ObjectMapperUtils.convertToObject(level, LevelDto.class);
    }

    @Override
    public FilterResponse<LevelDto> filter(LevelFilterRequest request) {
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<Level> page = levelRepository.filter(request, request.getPageable());
        List<LevelDto> dtos = ObjectMapperUtils.convertToList(page.getContent(), LevelDto.class);
        return PageUtils.toFilterResponse(page, dtos);
    }

    @Override
    public IDResponse<Long> create(LevelRequest request) {
        boolean exists = levelRepository.existsByCode(request.getCode());
        if (exists) {
            throw new AppException(CoreErrorCode.LEVEL_CODE_DUPLICATED);
        }
        Level level = ObjectMapperUtils.convertToObject(request, Level.class);
        level.setId(null);
        level.setIsDefault(false);
        levelRepository.save(level);
        return IDResponse.<Long>builder()
                .id(level.getId())
                .build();
    }

    @Override
    public IDResponse<Long> update(LevelRequest request) {
        Level level = levelRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(CoreErrorCode.LEVEL_NOT_FOUND));

        Level existsByCode = levelRepository.findByCode(request.getCode());
        if (existsByCode != null && !Objects.equals(level.getId(), existsByCode.getId())) {
            throw new AppException(CoreErrorCode.LEVEL_CODE_DUPLICATED);
        }
        level.setCode(request.getCode());
        level.setName(request.getName());
        levelRepository.save(level);
        return IDResponse.<Long>builder()
                .id(level.getId())
                .build();
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<Level> levels = levelRepository.findAllById(ids);
        if(ids.size() != levels.size()) {
            throw new AppException(CoreErrorCode.LEVEL_NOT_FOUND);
        }
        for(Level level : levels) {
            if(level.getIsDefault()){
                throw new AppException(CoreErrorCode.LEVEL_CANNOT_DELETE_DEFAULT);
            }
        }
        levelRepository.deleteAll(levels);
    }

    @Override
    public List<LevelDto> getByIds(List<Long> ids) {
        List<Level> levels = levelRepository.findAllById(ids);
        if(levels.isEmpty()) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(levels, LevelDto.class);
    }

    @Override
    public List<LevelDto> getLevelsByJobAdId(Long jobAdId) {
        List<Level> levels = levelRepository.findByJobAdId(jobAdId);
        if(levels.isEmpty()) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(levels, LevelDto.class);
    }

}
