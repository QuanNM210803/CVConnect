package com.cvconnect.service;

import com.cvconnect.dto.SocketSessionDto;

import java.util.List;
import java.util.Map;

public interface SocketSessionService {
    String createSocketSession(SocketSessionDto socketSession);
    void deleteSocketSession(String sessionId);
    Map<Long, List<SocketSessionDto>> getSocketSessionByUserIdIn(List<Long> ids);
}
