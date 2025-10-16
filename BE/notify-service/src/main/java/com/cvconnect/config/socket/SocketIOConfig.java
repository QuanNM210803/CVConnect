package com.cvconnect.config.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Value("${server.port-socket}")
    private int PORT_SOCKET;
    @Value("${server.socket-context}")
    private String SOCKET_CONTEXT;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setPort(PORT_SOCKET);
        configuration.setOrigin("*");
        configuration.setContext(SOCKET_CONTEXT);
        return new SocketIOServer(configuration);
    }
}
