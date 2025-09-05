package com.example.client.ws.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.client.domain.OnlineUser;
import com.example.client.service.OnlineStatusService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineStatusWebSocketHandler extends TextWebSocketHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(OnlineStatusWebSocketHandler.class);
    
    // 存储所有连接的WebSocket会话
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    
    // 存储用户ID与会话的映射
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    @Autowired
    private OnlineStatusService onlineStatusService;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
            onlineStatusService.setUserOnline(userId);
            logger.info("用户 {} 建立WebSocket连接", userId);
            // 广播最新的在线状态
            broadcastOnlineStatus();
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            onlineStatusService.setUserOffline(userId);
            logger.info("用户 {} 断开WebSocket连接", userId);
            // 广播最新的在线状态
            broadcastOnlineStatus();
        }
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息（如果需要）
        String payload = message.getPayload();
        logger.info("收到消息: {}", payload);
        
        // 可以根据消息类型进行不同处理
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(payload);
            String type = jsonNode.get("type").asText();
            
            if ("heartbeat".equals(type)) {
                // 处理心跳消息
                sendMessageToSession(session, "{\"type\":\"heartbeat\",\"data\":\"pong\"}");
            }
        } catch (Exception e) {
            logger.error("处理消息失败", e);
        }
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket传输错误", exception);
        sessions.remove(session);
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            onlineStatusService.setUserOffline(userId);
        }
    }
    
    // 广播在线状态信息
    public void broadcastOnlineStatus() {
        try {
            List<OnlineUser> onlineUsers = onlineStatusService.getAllOnlineUsers();
            String message = createOnlineStatusMessage(onlineUsers);
            
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    sendMessageToSession(session, buildOnlineStatusMessage());
                }
            }
        } catch (Exception e) {
            logger.error("广播在线状态失败", e);
        }
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
    
    // 向特定用户发送消息
    public void sendMessageToUser(String userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            sendMessageToSession(session, message);
        }
    }
    
    private void sendMessageToSession(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        }
    }
    
    private String getUserIdFromSession(WebSocketSession session) {
        // 从URL参数或者headers中获取用户ID
        Map<String, Object> attributes = session.getAttributes();
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            return query.split("userId=")[1].split("&")[0];
        }
        return null;
    }
    
    private String createOnlineStatusMessage(List<OnlineUser> onlineUsers) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> message = new HashMap<>();
        message.put("type", "online_status");
        message.put("data", onlineUsers);
        message.put("timestamp", System.currentTimeMillis());
        return mapper.writeValueAsString(message);
    }
}