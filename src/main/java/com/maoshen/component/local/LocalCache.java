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
public class LocalCache {
	/**
	 * 默认24小时过期
	 */
	private static Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(10000)
			.expireAfterWrite(24, TimeUnit.HOURS).recordStats().build();

	/**
	 * 获取值，如果获取不了，使用defaultValue默认值，如果defaultValue为空，则抛异常
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws ExecutionException
	 */
	public static Object get(String key,Object defaultValue) throws ExecutionException {
		Object var = cache.get(key, new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return defaultValue;
			}
		});
		return var;
	}
	public static Object get(String key) throws ExecutionException {
		return get(key,null);
	}

	public static void put(String key, Object value) {
		cache.put(key, value);
	}
}
