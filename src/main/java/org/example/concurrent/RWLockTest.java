package org.example.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockTest {
    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.writeLock().lock();
        lock.readLock().lock();
        lock.writeLock().unlock();
        lock.readLock().unlock();
    }
}
