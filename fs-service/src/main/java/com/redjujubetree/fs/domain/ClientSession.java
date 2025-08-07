package com.redjujubetree.fs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ClientSession {
    private String clientId;
//    private StreamObserver<ServerMessage> observer;
    private Instant lastActive = Instant.now();

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }
}
