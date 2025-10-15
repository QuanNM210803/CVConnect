package com.cvconnect.repository;

import com.cvconnect.collection.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ 'receiverId': ?0, 'isRead': false }")
    List<Notification> findByReceiverIdAndIsReadFalse(Long userId);

    @Query("{ 'id': ?0, 'receiverId': ?1 }")
    Notification findByIdAndReceiverId(String notificationId, Long userId);
}
