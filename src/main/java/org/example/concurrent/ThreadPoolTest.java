package org.example.concurrent;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
1. 线程池的作用
    避免频繁创建销毁线程带来的系统开销
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("this is thread pool task!");
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("this is thread pool task! executor");
            }
        });

    }
}
