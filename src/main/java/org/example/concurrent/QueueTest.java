package org.example.concurrent;

import java.util.concurrent.*;

public class QueueTest{
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
        queue.add(123);
        queue.add(456);
        queue.add(789);
        LinkedBlockingQueue queue2 = new LinkedBlockingQueue();
        queue2.add(12);
        queue2.add(456);
        queue2.add(789);
        queue2.add(123);
        queue2.take();

        DelayQueue delayQueue = new DelayQueue();
        delayQueue.put(new Delayed() {
            @Override
            public long getDelay(TimeUnit unit) {
                return 0;
            }

            @Override
            public int compareTo(Delayed o) {
                return 0;
            }
        });

        Delayed poll = delayQueue.poll();
        Delayed take = delayQueue.take();

    }
}
