package com.act.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    private WebSocketHandler chatEventHandler;

    @Autowired
    public WebSocketConfig(WebSocketHandler chatEventHandler) {
        this.chatEventHandler = chatEventHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatEventHandler, "/ws/chat")
            .setAllowedOrigins("*")
            .withSockJS();
    }
}
