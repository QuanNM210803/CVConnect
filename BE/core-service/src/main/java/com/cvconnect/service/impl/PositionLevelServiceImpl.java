package com.cvconnect.service.impl;

import com.cvconnect.repository.PositionLevelRepository;
import com.cvconnect.service.PositionLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionLevelServiceImpl implements PositionLevelService {
    @Autowired
    private PositionLevelRepository positionLevelRepository;
}
