package com.redjujubetree.fs.domain;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamContext {
        private final String streamId;
        private final AtomicBoolean completed = new AtomicBoolean(false);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final AtomicInteger pendingChunks = new AtomicInteger(0);
        private final CountDownLatch completionLatch = new CountDownLatch(1);
        private final long creationTime = System.currentTimeMillis();
        private volatile boolean clientCompleted = false;
        private volatile ScheduledFuture<?> timeoutTask;

        public StreamContext(String streamId) {
            this.streamId = streamId;
        }

        public boolean isActive() {
            return !completed.get() && !cancelled.get();
        }

        public void incrementPendingChunks() {
            if (isActive()) {
                pendingChunks.incrementAndGet();
            }
        }

        public void decrementPendingChunks() {
            if (pendingChunks.decrementAndGet() == 0 && clientCompleted) {
                completionLatch.countDown();
            }
        }

        public void markClientCompleted() {
            clientCompleted = true;
            if (pendingChunks.get() == 0) {
                completionLatch.countDown();
            }
        }

        public boolean waitForCompletion(long timeout, TimeUnit unit) throws InterruptedException {
            return completionLatch.await(timeout, unit);
        }

        public void markCompleted() {
            completed.set(true);
            if (timeoutTask != null) {
                timeoutTask.cancel(false);
            }
        }

        public void markCancelled() {
            cancelled.set(true);
            if (timeoutTask != null) {
                timeoutTask.cancel(false);
            }
        }

        public void setTimeoutTask(ScheduledFuture<?> timeoutTask) {
            this.timeoutTask = timeoutTask;
        }

        public long getAge() {
            return System.currentTimeMillis() - creationTime;
        }

    public String getStreamId() {
        return streamId;
    }

    public AtomicBoolean getCompleted() {
        return completed;
    }
    public boolean setCompleted(boolean completed) {
        return this.completed.compareAndSet(!completed, completed);
    }

    public AtomicBoolean getCancelled() {
        return cancelled;
    }
}