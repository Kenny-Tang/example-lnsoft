package com.redjujubetree.fs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session statistics DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatistics {
    private long activeSessions;
    private long completedSessions;
    private long failedSessions;
    private long expiredSessions;
    private long totalSessions;
    private double averageUploadProgress;
    private long totalUploadedSize;
    private Long oldestActiveSession;
}