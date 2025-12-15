package com.cvconnect.config.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.ChatMessageRequest;
import com.cvconnect.enums.RoomSocketType;
import com.cvconnect.service.ConversationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nmquan.commonlib.model.JwtUser;
import nmquan.commonlib.utils.JwtUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler {
    private final SocketIOServer server;

    private final ConversationService conversationService;
    private final Validator validator;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        try{
            String token = client.getHandshakeData().getSingleUrlParam("token");
            JwtUser jwtUser = JwtUtils.validate(token, SECRET_KEY);
            client.joinRoom(RoomSocketType.USER.getPrefix() + jwtUser.getUser().get("id").toString());

            log.info("Client connected: {}", client.getSessionId());
        } catch (Exception e) {
            log.error("Authentication fail: {}", client.getSessionId());
            client.disconnect();
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
    }

    @PostConstruct
    public void startServer() {
        server.start();
        server.addListeners(this);
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
    }

    @Async(Constants.BeanName.ASYNC_CONFIG)
    public void sendEvent(Object payload, String topic, String sessionId) {
        SocketIOClient client = server.getClient(UUID.fromString(sessionId));
        if (client != null) {
            client.sendEvent(topic, payload);
        }
    }

    @Async(Constants.BeanName.ASYNC_CONFIG)
    public void sendEventWithRoom(Object payload, String topic, String room) {
        Object payloadCopy = ObjectMapperUtils.convertToObject(payload, Object.class);
        server.getRoomOperations(room).sendEvent(topic, payloadCopy);
    }

    @OnEvent(Constants.SocketTopic.RECEIVE_MESSAGE)
    public void receiveMessage(SocketIOClient client, ChatMessageRequest data) {
        Set<ConstraintViolation<ChatMessageRequest>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            return;
        }
        String token = client.getHandshakeData().getSingleUrlParam("token");
        JwtUser jwtUser = JwtUtils.validate(token, SECRET_KEY);
        Map<String, Object> userInfo =  jwtUser.getUser();
        Object id = userInfo != null ? userInfo.get("id") : null;
        if(id == null) {
            return;
        }
        Long userId = Long.valueOf(id.toString());
        conversationService.newMessage(data, userId);
    }

}

