package com.maoshen.component.zookeeper.example;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.maoshen.component.zookeeper.BaseZookeeper;

public class ExampleZookeeper extends BaseZookeeper {

	public ExampleZookeeper(String ipUrl, String mainTtile) throws Exception {
		super(ipUrl, mainTtile);
	}

	@Override
	public void runWatcher(WatchedEvent event) {
		System.out.println(new Random().nextInt(10000) + "," + "jdx1112回调watcher实例： 路径" + event.getPath() + " 类型："
				+ event.getType());
		if(StringUtils.isNotBlank(event.getPath())){
			//正式的工作
		}
	}

	@Override
	public boolean isAutoRefresh() {
		return true;
	}

	public static void main(String[] s) throws Exception {
		String url = "192.168.30.183:2181";
		new ExampleZookeeper(url, "jdx111222");

		ZooKeeper zookeeper = null;
		try {
			zookeeper = new ZooKeeper(url, 10000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {

				}
			});
			zookeeper.setData("/jdx111222", Long.toString(System.currentTimeMillis()).getBytes(), -1);
			Thread.sleep(2000);
			zookeeper.setData("/jdx111222", Long.toString(System.currentTimeMillis()).getBytes(), -1);
			Thread.sleep(2000);
			zookeeper.setData("/jdx111222", Long.toString(System.currentTimeMillis()).getBytes(), -1);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(zookeeper!=null){
				zookeeper.close();
			}
		}
	}
}
