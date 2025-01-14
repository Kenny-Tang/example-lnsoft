package org.example.concurrent;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 200,60000, TimeUnit.SECONDS,null);

    static class LocalVariable{
        byte[] obj = new byte[1024*1024*5];
    }
    public static void main(String[] args) {


    }
}
