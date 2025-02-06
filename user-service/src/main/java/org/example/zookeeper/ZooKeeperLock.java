package org.example.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class ZooKeeperLock {

	public static void main(String[] args) {
		CuratorFramework zkClient = ZooKeeperUtil.createCuratorFramework();
		InterProcessMutex interProcessMutex = new InterProcessMutex(zkClient, "/lock");
		for (int i = 0; i < 10; i++) {
			new ZooKeeperLockThread(interProcessMutex, i).start();
		}
	}
}

class ZooKeeperLockThread extends Thread {
	private Integer threadId ;
	private InterProcessMutex interProcessMutex ;
	public ZooKeeperLockThread(InterProcessMutex interProcessMutex, Integer threadId) {
		this.threadId = threadId;
		this.interProcessMutex = interProcessMutex;
	}

	@Override
	public void run() {
		try {
			interProcessMutex.acquire();
			System.out.println("threadId = " + threadId + ", 获取了所权限");
			Thread.sleep(20000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				interProcessMutex.release();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
