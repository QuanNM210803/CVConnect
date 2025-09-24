package com.cvconnect.service.impl;

import com.cvconnect.repository.PositionRepository;
import com.cvconnect.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;
}
