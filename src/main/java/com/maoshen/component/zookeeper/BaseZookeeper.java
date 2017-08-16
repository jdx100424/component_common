package com.maoshen.component.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 常规zookeeper
 * @author daxian.jianglifesense.com
 *
 */
public abstract class BaseZookeeper {
	private static final Object object = new Object();
	
	protected ZooKeeper zooKeeper;
	
	private static final int TIME_OUT = 5000;

	public BaseZookeeper(String ipUrl) throws Exception {
		this(TIME_OUT,ipUrl);
	}
	
	public BaseZookeeper(int timeout,String ipUrl) throws Exception {
		zooKeeper = new ZooKeeper(ipUrl, timeout,new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				synchronized(object){
					runWatcher(event);
				}
			}
		});
	}
	
	public abstract void runWatcher(WatchedEvent event);
}
