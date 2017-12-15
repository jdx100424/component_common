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
public class LocalCacheObjectUtils {
	public static Cache<String, Object> getInstance(){
		return CacheBuilder.newBuilder().maximumSize(10000)
				.expireAfterWrite(24, TimeUnit.HOURS).recordStats().build();
	}
	public static Cache<String, Object> getInstance(int maxSize,long time,TimeUnit timeUnit){
		return CacheBuilder.newBuilder().maximumSize(maxSize)
				.expireAfterWrite(time,timeUnit).recordStats().build();
	}
	
	/**
	 * 获取值，如果获取不了，使用defaultValue默认值，如果defaultValue为空，则抛异常
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws ExecutionException
	 */
	public static Object get(Cache<String, Object> cache , String key,Object defaultValue) throws ExecutionException {
		Object var = cache.get(key, new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return defaultValue;
			}
		});
		return var;
	}
	public static Object get(Cache<String, Object> cache ,String key) throws ExecutionException {
		return get(cache,key,null);
	}

	public static void put(Cache<String, Object> cache ,String key, Object value) {
		cache.put(key, value);
	}
	
	public static long size(Cache<String, Object> cache){
		return cache.size();
	}
}
