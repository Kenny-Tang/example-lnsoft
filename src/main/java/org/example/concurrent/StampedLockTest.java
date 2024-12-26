package org.example.concurrent;

import java.util.concurrent.Semaphore;

public class StampedLockTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        semaphore.release();

    }
}
