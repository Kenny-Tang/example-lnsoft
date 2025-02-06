package org.example.concurrent;

public class PrintABCSync {
    private static volatile int state = 0;
    private static Class lock = PrintABCSync.class;
    public static void main(String[] args) {
        int whileCount = 10000;
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < whileCount; i++) {
                synchronized (lock) {
                    while (state%3 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.print("A");
                    state++;
                    lock.notifyAll();
                }
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < whileCount; i++) {
                synchronized (lock) {
                    while (state%3 != 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.print("B");
                    state++;
                    lock.notifyAll();
                }
            }
        });
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < whileCount; i++) {
                synchronized (lock) {
                    while (state%3 != 2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("C");
                    state++;
                    lock.notifyAll();
                }
            }
        });
        threadC.start();
        threadB.start();
        threadA.start();
    }
}
