package com.maoshen.component.other;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存本地map实现
 * @description <br>
 * @author <a href="mailto:wei.jiang@lifesense.com">vakin</a>
 * @date 2015年10月28日
 * Copyright (c) 2015, lifesense.com
 */
public class MapCacheProvider {
	
	
	static Map<String, 	Object> cache = new ConcurrentHashMap<>();
	static Map<String, 	Date> cacheExpire = new HashMap<>();
	
	private Timer timer;
	
	public MapCacheProvider() {
		this(5);
	}
	
	/**
	 * @param period 检查过期间隔（秒）
	 */
	public MapCacheProvider(long period) {
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			public void run() {
				Date now = Calendar.getInstance().getTime();
				
				synchronized (cacheExpire) {
					Iterator<Map.Entry<String, 	Date>> it = cacheExpire.entrySet().iterator();  
					while(it.hasNext()){  
						Map.Entry<String, Date> entry=it.next();  
						//过期的移除
						if(entry.getValue().compareTo(now)<=0){
							cache.remove(entry.getKey());
							it.remove(); 
						}  
					}  
				}
			}
		}, 1000, period*1000);
	}

	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param timeout 单位：秒
	 * @return
	 */
	public boolean set(String key, Object value, int timeout) {
		Date expireAt = timeout > -1 ? DateUtils.add(new Date(), Calendar.SECOND, timeout) : null;
		return set(key, value, expireAt);
	}

	
	public boolean set(String key, Object value, Date expireAt) {
		cache.put(key, value);
		if(expireAt != null){
			cacheExpire.put(key, expireAt);
		}
		return true;
	}

	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T)cache.get(key);
	}

	
	public boolean remove(String key) {
		cache.remove(key);
		cacheExpire.remove(key);
		return true;
	}


	
	public boolean exists(String key) {
		return cache.containsKey(key);
	}
	

}
