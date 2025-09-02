package com.cvconnect.service;

import com.cvconnect.dto.SocketSessionDto;

import java.util.Map;

public interface SocketSessionService {
    String createSocketSession(SocketSessionDto socketSession);
    void deleteSocketSession(String sessionId);
    Map<String, SocketSessionDto> getSocketSessionByUserId(Long userId);
}
