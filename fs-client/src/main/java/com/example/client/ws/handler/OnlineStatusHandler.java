package com.example.client.ws.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class OnlineStatusHandler extends TextWebSocketHandler {
    
    private static final Set<WebSocketSession> sessions =
        Collections.synchronizedSet(new HashSet<>());
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        // 发送当前在线状态
        broadcastOnlineStatus();
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        broadcastOnlineStatus();
    }
    
    public void broadcastOnlineStatus() {
        String message = buildOnlineStatusMessage();
        sessions.parallelStream().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                sessions.remove(session);
            }
        });
    }
    
    private String buildOnlineStatusMessage() {
        // 构建在线状态消息
        Map<String, Object> status = new HashMap<>();
        status.put("type", "online_status");
        status.put("onlineTime", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMATTER));
        status.put("onlineStatus", "online" );
        status.put("timestamp", System.currentTimeMillis());
        return JSONUtil.toJsonStr(status);
    }
}