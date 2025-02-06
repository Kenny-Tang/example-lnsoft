package org.example.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class PrintABCAtomicInteger {
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; ) {
                if (atomicInteger.get() % 3 == 0) {
                    System.out.print("A");
                    atomicInteger.addAndGet(1);
                    i++;
                }
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10; ) {
                if (atomicInteger.get() % 3 == 1) {
                    System.out.print("B");
                    atomicInteger.addAndGet(1);
                    i++;
                }
            }
        });
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10; ) {
                if (atomicInteger.get() % 3 == 2) {
                    System.out.println("C");
                    atomicInteger.addAndGet(1);
                    i++;
                }
            }
        });
        threadA.start();
        threadB.start();
        threadC.start();
    }
}
