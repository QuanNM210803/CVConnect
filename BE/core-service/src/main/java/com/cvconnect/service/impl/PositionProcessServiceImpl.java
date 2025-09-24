package com.cvconnect.service.impl;

import com.cvconnect.repository.PositionProcessRepository;
import com.cvconnect.service.PositionProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionProcessServiceImpl implements PositionProcessService {
    @Autowired
    private PositionProcessRepository positionProcessRepository;
}
