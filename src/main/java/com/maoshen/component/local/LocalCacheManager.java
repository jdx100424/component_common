package com.maoshen.component.local;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 本地缓存
 * @author daxian.jianglifesense.com
 *
 */
public class LocalCacheManager {
	protected Cache<String, Object> cache;
	
	protected LocalCacheManager(Cache<String, Object> cache){
		this.cache = cache;
	}
	
	public static LocalCacheManager getInstance(){
		return getInstance(10000,24, TimeUnit.HOURS);
	}
	public static LocalCacheManager getInstance(int maxSize,long time,TimeUnit timeUnit){
		Cache<String, Object> newCache = CacheBuilder.newBuilder().maximumSize(maxSize)
				.expireAfterWrite(time,timeUnit).recordStats().build();
		return new LocalCacheManager(newCache);
	}
	
	/**
	 * 获取值，如果获取不了，使用defaultValue默认值，如果defaultValue为空，则抛异常
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws ExecutionException
	 */
	public Object get(String key,Object defaultValue){
		Object var = null;
		try{
			var = cache.get(key, new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return defaultValue;
				}
			});
		}catch(Exception e){
		}
		return var;
	}
	public Object get(String key) {
		return get(key,null);
	}

	public void put(String key, Object value) {
		cache.put(key, value);
	}
	
	public long size(){
		return cache.size();
	}
}
