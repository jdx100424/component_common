package com.maoshen.component.zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 常规zookeeper
 * @author daxian.jianglifesense.com
 *
 */
public class BaseZookeeper {
	protected ZooKeeper zooKeeper;
	
	private static final int TIME_OUT = 5000;
	
	public BaseZookeeper(String ipUrl) throws Exception {
		this(null,ipUrl);
	}

	public BaseZookeeper(int timeout,String ipUrl) throws Exception {
		this(null,timeout,ipUrl);
	}
	
	public BaseZookeeper(Watcher watcher,String ipUrl) throws Exception {
		this(watcher,TIME_OUT,ipUrl);
	}
	
	public BaseZookeeper(Watcher watcher, int timeout,String ipUrl) throws Exception {
		super();
		zooKeeper = new ZooKeeper(ipUrl, timeout,watcher);
	}
}
