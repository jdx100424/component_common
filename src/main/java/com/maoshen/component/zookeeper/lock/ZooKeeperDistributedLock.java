package com.maoshen.component.zookeeper.lock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDistributedLock {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDistributedLock.class);

	private ZooKeeper zookeeper;
	private String rootPath = "/locks";
	
	private String lockName;
	private String trueLockName;
	
	private int lockTimeout = 10;
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	public ZookeeperDistributedLock(ZooKeeper zookeeper,String lockName) {
		super();
		this.zookeeper = zookeeper;
		this.lockName = lockName;
		ensureRoot();
	}

	private void ensureRoot() {
		try {
			if (Objects.isNull(zookeeper.exists(rootPath, false))) {
				zookeeper.create(rootPath, "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

	public void tryLock() {
		try {
			String lockPath = rootPath + "/" + lockName;
			String myPath = zookeeper.create(lockPath, "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			BlockingQueue<String> lock = new ArrayBlockingQueue<>(1);
			lock(lockPath, myPath, lock);
			if (lock.poll(lockTimeout, timeUnit) != null) {
				this.trueLockName = myPath;
				return;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		this.trueLockName = null;
		//return false;
	}

	public void unLock() {
		try {
			if (Objects.nonNull(zookeeper.exists(trueLockName, false))) {
				zookeeper.delete(trueLockName, -1);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

	private void lock(String lockPath, String myPath, BlockingQueue<String> lock) {
		try {
			List<String> children = zookeeper.getChildren(rootPath, false);
			Collections.sort(children);
			String littleThanMe = null;
			String myName = myPath.substring(myPath.lastIndexOf("/") + 1, myPath.length());
			for (String child : children) {
				if (myName.equals(child) && Objects.isNull(littleThanMe)) {
					lock.put("lock");
					return;
				} else if (child.compareTo(myName) < 0) {
					littleThanMe = child;
				} else {
					break;
				}
			}
			watchLittleThanMe(lockPath, myPath, lock, rootPath + "/" + littleThanMe);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

	private void watchLittleThanMe(String lockPath, String myPath, BlockingQueue<String> lock, String littleThanMe)
			throws KeeperException, InterruptedException {
		try {
			zookeeper.exists(littleThanMe, event -> {
				if (event.getType() == EventType.NodeDeleted) {
					lock(lockPath, myPath, lock);
				}
			});
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			lock(lockPath, myPath, lock);
		}
	}

	public static void main(String[] args) throws Exception {
		int cunrrent = 5;
		System.out.println("start:"+new java.util.Date());
		for (int i = 0; i < cunrrent; i++) {
			new Thread(){
				public void run(){
					ZookeeperDistributedLock lock = null;
					try {
						lock = new ZookeeperDistributedLock(new ZooKeeper("192.168.30.183:2181", 60000, null),"jdx000");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					lock.tryLock();
					System.out.println(new java.util.Date()+",running ! path : " + lock.trueLockName + " ,thread : " + Thread.currentThread().getName());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.unLock();
				}
			}.start();
		}
		System.out.println("end:"+new java.util.Date());
		Thread.sleep(10000);
	}
}