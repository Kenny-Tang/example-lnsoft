package org.example.concurrent;

import java.util.concurrent.Semaphore;

public class PrintABCSemaphore {
    private final static Semaphore semaphoreA = new Semaphore(1);
    private final static Semaphore semaphoreB = new Semaphore(0);
    private final static Semaphore semaphoreC = new Semaphore(0);

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreA.acquire();
                    System.out.print("A");
                    semaphoreB.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreB.acquire();
                    System.out.print("B");
                    semaphoreC.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreC.acquire();
                    System.out.println("C");
                    semaphoreA.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadC.start();
        threadB.start();
        threadA.start();

    }
}