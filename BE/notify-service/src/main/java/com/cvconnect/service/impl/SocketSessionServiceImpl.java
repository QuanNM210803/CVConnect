package com.cvconnect.service.impl;

import com.cvconnect.collection.SocketSession;
import com.cvconnect.dto.SocketSessionDto;
import com.cvconnect.repository.SocketSessionRepository;
import com.cvconnect.service.SocketSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SocketSessionServiceImpl implements SocketSessionService {
    @Autowired
    private SocketSessionRepository socketSessionRepository;

    @Override
    public String createSocketSession(SocketSessionDto dto) {
        SocketSession socketSession = new SocketSession();
        socketSession.setSessionId(dto.getSessionId());
        socketSession.setUserId(dto.getUserId());
        socketSession.setCreatedAt(dto.getCreatedAt());
        socketSessionRepository.save(socketSession);
        return socketSession.getSessionId();
    }

    @Override
    public void deleteSocketSession(String sessionId) {
        socketSessionRepository.deleteBySessionId(sessionId);
    }

    @Override
    public Map<String, SocketSessionDto> getSocketSessionByUserId(Long userId) {
        return socketSessionRepository.findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(SocketSession::getSessionId,
                        socketSession -> SocketSessionDto.builder()
                                                        .id(socketSession.getId())
                                                        .sessionId(socketSession.getSessionId())
                                                        .userId(socketSession.getUserId())
                                                        .createdAt(socketSession.getCreatedAt())
                                                        .build()
                ));
    }
}
