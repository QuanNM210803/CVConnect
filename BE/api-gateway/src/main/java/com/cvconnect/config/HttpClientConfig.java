package com.cvconnect.config;

import com.cvconnect.repository.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {
    @Value("${server.host_user_service}")
    private String HOST_AUTH;

    @Value("${server.port_user_service}")
    private String PORT_AUTH;

    @Bean
    public WebClient authWebClient() {
        return WebClient.builder()
                .baseUrl("http://" + HOST_AUTH + ":" + PORT_AUTH)
                .build();
    }
    @Bean
    AuthClient authClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(AuthClient.class);
    }
}
