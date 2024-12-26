package com.redjujubetree.example.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 16, 10L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000),threadFactory, new ThreadPoolExecutor.AbortPolicy());
	public static void execute(Runnable runnable) {
		executor.execute(runnable);
	}
}
