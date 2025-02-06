package org.example.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;


public class ZooKeeperTest {
	private static final String connectString = "172.16.118.101:2181";
	private static ZooKeeper zooKeeper = null;
	static {
		zooKeeper = createZooKeeperClient();
	}
	public static void main(String[] args) throws Exception {
		String path = "/watcher";
		Stat exists = zooKeeper.exists(path, false);
		if (exists == null) {
			zooKeeper.create(path, "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		LockSupport.parkNanos(1000*1000);
		Stat stat=zooKeeper.exists(path,true);
		int read = System.in.read();
	}

	private static String getZooKeeperConfigInfoByCurator(String path) throws Exception {
		CuratorFramework framework = createCuratorFramework();
		framework.start();
		byte[] bytes = framework.getData().storingStatIn(new Stat()).forPath(path);
		framework.close();
		return new String(bytes);
	}

	private static CuratorFramework createCuratorFramework() {
		CuratorFramework build = CuratorFrameworkFactory.builder().connectString(connectString).sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("").build();
		return build;
	}

	public static ZooKeeper createZookeeperNode(String path, String data) throws Exception {
		ZooKeeper zooKeeper = createZooKeeperClient();
		zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		return zooKeeper;
	}

	private static ZooKeeper createZooKeeperClient() {
		try {
			ZooKeeper zooKeeper = new ZooKeeper(connectString, 6000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.err.print("Path: " + event.getPath());
					System.err.print("State" + event.getState());
					System.err.println("Type" + event.getType());
				}
			});
			return zooKeeper;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
