package com.cvconnect.repository;

import com.cvconnect.collection.SocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocketSessionRepository extends MongoRepository<SocketSession, String> {
    void deleteBySessionId(String sessionId);

    @Query("{ 'userId': { $in: ?0 } }")
    List<SocketSession> findAllByUserIdIn(List<Long> ids);
}
