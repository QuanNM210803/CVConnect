package com.cvconnect.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.cvconnect.collection.Notification;
import com.cvconnect.dto.NotificationDto;
import com.cvconnect.dto.SocketSessionDto;
import com.cvconnect.repository.NotificationRepository;
import com.cvconnect.service.NotificationService;
import com.cvconnect.service.SocketSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SocketSessionService socketSessionService;
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void pushNotification(NotificationDto notificationDto) {
        if(ObjectUtils.isEmpty(notificationDto)){
            return;
        }
        if(ObjectUtils.isEmpty(notificationDto.getReceiverIds())){
            return;
        }
        this.save(notificationDto);

//        Map<Long, List<String>> userSocketMap = socketSessionService
//                .getSocketSessionByUserIdIn(notificationDto.getReceiverIds())
//                .values()
//                .stream()
//                .collect(Collectors.groupingBy(
//                        SocketSessionDto::getUserId,
//                        Collectors.mapping(SocketSessionDto::getSessionId, Collectors.toList())
//                ));
//
//        NotificationDto payload = new NotificationDto(notificationDto);
//        payload.setReceiverIds(null);
//
//        userSocketMap.forEach((userId, sessionIds) -> {
//            sessionIds.forEach(sessionId -> {
//                SocketIOClient client = socketIOServer.getClient(UUID.fromString(sessionId));
//                if (client != null) {
//                    client.sendEvent("message", payload);
//                }
//            });
//        });
    }

    @Override
    public void save(NotificationDto notificationDto) {
        for(Long receiverId : notificationDto.getReceiverIds()) {
            Notification notification = new Notification();
            notification.setTitle(notificationDto.getTitle());
            notification.setMessage(notificationDto.getMessage());
            notification.setSenderId(notificationDto.getSenderId());
            notification.setReceiverId(receiverId);
            notification.setType(notificationDto.getType());
            notification.setRedirectUrl(notificationDto.getRedirectUrl());
            notification.setIsRead(false);
            notification.setReadAt(null);
            notification.setCreatedAt(notificationDto.getCreatedAt() == null ? Instant.now() : notificationDto.getCreatedAt());
            notificationRepository.save(notification);
        }
    }
}
