package com.redjujubetree.fs.queue;

@FunctionalInterface
public interface MessageRepublisher {
    void republish(QueueMessage msg);
}
