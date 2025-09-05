package com.example.client.service;

import com.example.client.domain.OnlineUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineStatusService {
    
    // 使用Redis存储在线用户信息，这里用内存Map简化
    private final Map<String, OnlineUser> onlineUsers = new ConcurrentHashMap<>();
    
    public void setUserOnline(String userId) {
        OnlineUser user = new OnlineUser();
        user.setUserId(userId);
        user.setStatus("online");
        user.setLastActiveTime(new Date());
        onlineUsers.put(userId, user);
    }
    
    public void setUserOffline(String userId) {
        onlineUsers.remove(userId);
    }
    
    public List<OnlineUser> getAllOnlineUsers() {
        return new ArrayList<>(onlineUsers.values());
    }
    
    public boolean isUserOnline(String userId) {
        return onlineUsers.containsKey(userId);
    }
    
    public int getOnlineCount() {
        return onlineUsers.size();
    }
}