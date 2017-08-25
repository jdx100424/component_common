package com.maoshen.component.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常规zookeeper
 * @author daxian.jianglifesense.com
 *
 */
public abstract class BaseZookeeper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseZookeeper.class);

	private static final Object object = new Object();
	
	protected ZooKeeper zooKeeper;
	
	private static final int TIME_OUT = 5000;

	/**
	 * 
	 * @param ipUrl,zookeeper地址
	 * @param mainTitle，主目录，用于激活Watcher
	 * @throws Exception
	 */
	public BaseZookeeper(String ipUrl,String mainTitle) throws Exception {
		this(TIME_OUT,ipUrl,mainTitle);
	}
	
	public BaseZookeeper(int timeout,String ipUrl,String mainTitle) throws Exception {
		if(StringUtils.isBlank(mainTitle)){
			throw new Exception("zookeeper mainTitle is not allow null");
		}
		zooKeeper = new ZooKeeper(ipUrl, timeout,new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				synchronized(object){
					runWatcher(event);
					if(isAutoRefresh() && StringUtils.isNotBlank(event.getPath())){
						try {
							zooKeeper.exists(event.getPath(), true);
						} catch (Exception e) {
							LOGGER.error(e.getMessage(),e);
						}
					}
				}
			}
		});
		Stat stat = zooKeeper.exists("/"+mainTitle, isAutoRefresh());
		if(stat==null){
			zooKeeper.create("/"+mainTitle, "".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
			zooKeeper.exists("/"+mainTitle, isAutoRefresh());
		}
	}
	
	public abstract void runWatcher(WatchedEvent event);
	
	/**
	 * 每个监听器工作完成后，是否自动刷新保持永久能监听
	 * @return
	 */
	public abstract boolean isAutoRefresh();
}
