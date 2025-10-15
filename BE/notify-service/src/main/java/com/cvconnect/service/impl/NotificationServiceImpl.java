package com.cvconnect.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.cvconnect.collection.Notification;
import com.cvconnect.config.socket.SocketHandler;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.NotificationDto;
import com.cvconnect.dto.NotificationFilterRequest;
import com.cvconnect.dto.SocketSessionDto;
import com.cvconnect.repository.NotificationRepository;
import com.cvconnect.service.MongoQueryService;
import com.cvconnect.service.NotificationService;
import com.cvconnect.service.SocketSessionService;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SocketSessionService socketSessionService;
    @Autowired
    private MongoQueryService mongoQueryService;
    @Autowired
    private SocketHandler socketHandler;

    @Override
    public void pushNotification(NotificationDto notificationDto) {
        if(ObjectUtils.isEmpty(notificationDto)){
            return;
        }
        if(ObjectUtils.isEmpty(notificationDto.getReceiverIds())){
            return;
        }
        List<Notification> notifications = this.save(notificationDto);

        Map<Long, List<SocketSessionDto>> userSocketMap = socketSessionService
                .getSocketSessionByUserIdIn(notificationDto.getReceiverIds());

        List<NotificationDto> payloads = ObjectMapperUtils.convertToList(notifications, NotificationDto.class);
        Map<Long, NotificationDto> payloadMap = payloads.stream()
                .collect(Collectors.toMap(
                        NotificationDto::getReceiverId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        userSocketMap.forEach((userId, sessions) -> {
            NotificationDto payload = payloadMap.get(userId);
            sessions.forEach(session -> socketHandler.pushToSocket(payload, Constants.SocketTopic.NOTIFY, session.getSessionId()));
        });
    }

    @Override
    public List<Notification> save(NotificationDto notificationDto) {
        List<Notification> savedNotifications = new ArrayList<>();
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
            savedNotifications.add(notification);
        }
        return savedNotifications;
    }

    @Override
    public FilterResponse<NotificationDto> getMyNotifications(NotificationFilterRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        request.setReceiverId(userId);
        Page<Notification> page = mongoQueryService.findNotificationWithFilter(request, request.getPageable());
        List<NotificationDto> dtos = ObjectMapperUtils.convertToList(page.getContent(), NotificationDto.class);
        return PageUtils.toFilterResponse(page, dtos);
    }

    @Override
    public Long getQuantityUnread() {
        NotificationFilterRequest request = new NotificationFilterRequest();
        request.setReceiverId(WebUtils.getCurrentUserId());
        request.setIsRead(false);
        request.setPageIndex(0);
        request.setPageSize(Integer.MAX_VALUE);
        Page<Notification> page = mongoQueryService.findNotificationWithFilter(request, request.getPageable());
        return page.getTotalElements();
    }

    @Override
    public void markAllAsRead() {
        Long userId = WebUtils.getCurrentUserId();
        List<Notification> notifications = notificationRepository.findByReceiverIdAndIsReadFalse(userId);
        if(!ObjectUtils.isEmpty(notifications)){
            notifications.forEach(n -> {
                n.setIsRead(true);
                n.setReadAt(Instant.now());
            });
            notificationRepository.saveAll(notifications);
        }
        Map<Long, List<SocketSessionDto>> userSocketMap = socketSessionService.getSocketSessionByUserIdIn(List.of(userId));
        List<SocketSessionDto> sessions = userSocketMap.get(userId);
        if(ObjectUtils.isEmpty(sessions)){
            return;
        }
        Map<String, Object> payload = Map.of(
                "quantityUnread", 0L
        );
        sessions.forEach(session -> socketHandler.pushToSocket(payload, Constants.SocketTopic.UNREAD_NOTIFY, session.getSessionId()));
    }

    @Override
    public void markAsRead(String notificationId) {
        Long userId = WebUtils.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId);
        if(notification != null && !notification.getIsRead()){
            notification.setIsRead(true);
            notification.setReadAt(Instant.now());
            notificationRepository.save(notification);

            Map<Long, List<SocketSessionDto>> userSocketMap = socketSessionService.getSocketSessionByUserIdIn(List.of(userId));
            List<SocketSessionDto> sessions = userSocketMap.get(userId);
            if(ObjectUtils.isEmpty(sessions)){
                return;
            }
            Long quantityUnread = this.getQuantityUnread();
            Map<String, Object> payload = Map.of(
                    "quantityUnread", quantityUnread,
                    "notificationId", notificationId
            );
            sessions.forEach(session -> socketHandler.pushToSocket(payload, Constants.SocketTopic.UNREAD_NOTIFY, session.getSessionId()));
        }
    }
}
