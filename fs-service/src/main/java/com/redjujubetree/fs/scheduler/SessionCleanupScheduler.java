package com.redjujubetree.fs.scheduler;

import com.redjujubetree.fs.domain.dto.SessionStatistics;
import com.redjujubetree.fs.service.StorageSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Scheduled task for session cleanup
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.session.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public class SessionCleanupScheduler {
    
    private final StorageSessionService sessionService;
    
    @Value("${storage.session.cleanup.timeout:PT6H}")
    private Duration cleanupTimeout;
    
    @Scheduled(fixedDelayString = "${storage.session.cleanup.interval:PT1H}")
    public void cleanupExpiredSessions() {
        try {
            log.info("Starting scheduled cleanup of expired sessions...");
            
            int cleanedCount = sessionService.cleanupExpiredSessions(cleanupTimeout);
            
            if (cleanedCount > 0) {
                log.info("Cleaned up {} expired sessions", cleanedCount);
            } else {
                log.debug("No expired sessions found to clean up");
            }
            
        } catch (Exception e) {
            log.error("Error during scheduled session cleanup", e);
        }
    }
    
    @Scheduled(fixedDelayString = "${storage.session.stats.interval:PT30M}")
    public void logSessionStatistics() {
        try {
            SessionStatistics stats = sessionService.getSessionStatistics();
            log.info("Session Statistics - Active: {}, Completed: {}, Failed: {}, Expired: {}, Total: {}, Avg Progress: {:.2f}%",
                stats.getActiveSessions(),
                stats.getCompletedSessions(), 
                stats.getFailedSessions(),
                stats.getExpiredSessions(),
                stats.getTotalSessions(),
                stats.getAverageUploadProgress());
                
        } catch (Exception e) {
            log.error("Error logging session statistics", e);
        }
    }
}