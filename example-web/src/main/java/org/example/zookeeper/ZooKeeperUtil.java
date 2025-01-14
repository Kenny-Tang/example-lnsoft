package org.example.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperUtil {
	private static final String connectString = "172.16.118.101:2181";

	public static CuratorFramework createCuratorFramework() {
		CuratorFramework build = CuratorFrameworkFactory.builder().connectString(connectString).sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("").build();
		build.start() ;
		return build;
	}
}
