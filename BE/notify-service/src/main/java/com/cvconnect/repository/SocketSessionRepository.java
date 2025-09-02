package com.cvconnect.repository;

import com.cvconnect.collection.SocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocketSessionRepository extends MongoRepository<SocketSession, String> {
    void deleteBySessionId(String sessionId);
    List<SocketSession> findAllByUserId(Long userId);
}
