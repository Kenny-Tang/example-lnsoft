package org.example.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABCCondition {

    static int state = 0;
    static ReentrantLock lock = new ReentrantLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();
    static Condition conditionC = lock.newCondition();
    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                while (state % 3 != 0) {
                    try {
                        conditionA.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print("A");
                state++;
                conditionB.signal();
                lock.unlock();
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                while (state % 3 != 1) {
                    try {
                        conditionB.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print("B");
                state++;
                conditionC.signal();
                lock.unlock();
            }
        });
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                while (state % 3 != 2) {
                    try {
                        conditionC.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("C");
                state++;
                conditionA.signal();
                lock.unlock();
            }
        });
        threadA.start();
        threadB.start();
        threadC.start();
    }
}
