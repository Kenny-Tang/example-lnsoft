package com.example.client.config;

import com.example.client.ws.handler.OnlineStatusWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private OnlineStatusWebSocketHandler onlineStatusWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(onlineStatusWebSocketHandler, "/ws/online")
                .setAllowedOrigins("*"); // 生产环境建议设置具体域名
    }
}