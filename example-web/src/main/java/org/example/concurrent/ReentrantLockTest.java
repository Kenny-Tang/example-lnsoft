package org.example.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        FixedQueue queue = new FixedQueue();

        Thread producer = new Thread(new Producer(queue));
        producer.start();
        Thread producer1 = new Thread(new Producer(queue));
        producer1.start();
        Thread consumer = new Thread(new Consumer(queue));
        consumer.start();

    }
}
class Producer implements Runnable {
    static AtomicInteger counter = new AtomicInteger(0) ;
    FixedQueue queue;
    public Producer(FixedQueue queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        while (true) {
            try {
                int andAdd = counter.getAndAdd(1);
                queue.put(Thread.currentThread().getName() + "_" + andAdd);
                LockSupport.parkNanos(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Consumer implements Runnable {
    FixedQueue queue;
    public Consumer(FixedQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        while(true){
            try {
                Object take = queue.take();
                Thread thread = Thread.currentThread();
                thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class FixedQueue {
    private int tableSize = 10;
    Object[] objects;
    public int size=0;
    private final ReentrantLock lock;
    Condition producer;
    Condition consumer;
    private int producerPointer = 0;
    private int consumerPointer = 0;
    FixedQueue(){
        objects = new Object[tableSize];
        lock = new ReentrantLock();
        consumer = lock.newCondition();
        producer = lock.newCondition();
    }

    public void put(Object newPlate) throws InterruptedException {
        lock.lock();
        try {
            while (size==tableSize) {
                producer.await();
            }
            producerPointer = producerPointer % tableSize;
            objects[producerPointer] = newPlate;
            producerPointer++;
            size++;
            //System.out.println("Producer : " + newPlate);
            consumer.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (size==0) {
                consumer.await();
            }
            consumerPointer = consumerPointer % tableSize;
            Object ret = objects[consumerPointer];
            consumerPointer++;
            size--;
            System.out.println("Consumer : " + ret);
            producer.signal();
            return ret;
        } finally {
            lock.unlock();
        }
    }
}
