package com.example.client.controller;

import com.example.client.domain.OnlineUser;
import com.example.client.service.OnlineStatusService;
import com.example.client.ws.handler.OnlineStatusHandler;
import com.example.client.ws.handler.OnlineStatusWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/online")
public class OnlineStatusController {
    
    @Autowired
    private OnlineStatusService onlineStatusService;
    
    @Autowired
    private OnlineStatusHandler onlineStatusHandler;

    @Resource
    private OnlineStatusWebSocketHandler onlineStatusWebSocketHandler;
    
    @GetMapping("/users")
    public ResponseEntity<List<OnlineUser>> getOnlineUsers() {
        List<OnlineUser> onlineUsers = onlineStatusService.getAllOnlineUsers();
        return ResponseEntity.ok(onlineUsers);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Integer> getOnlineCount() {
        int count = onlineStatusService.getOnlineCount();
        return ResponseEntity.ok(count);
    }
    
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastMessage(@RequestBody Map<String, String> request) {
        // 手动触发广播
        onlineStatusWebSocketHandler.broadcastOnlineStatus();
        return ResponseEntity.ok("广播成功");
    }
}