package com.example.client.domain;

import java.util.Date;

public class OnlineUser {
    private String userId;
    private String status;
    private Date lastActiveTime;
    private String sessionId;
    
    // 构造函数、getter和setter
    public OnlineUser() {}
    
    public OnlineUser(String userId, String status) {
        this.userId = userId;
        this.status = status;
        this.lastActiveTime = new Date();
    }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getLastActiveTime() { return lastActiveTime; }
    public void setLastActiveTime(Date lastActiveTime) { this.lastActiveTime = lastActiveTime; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}