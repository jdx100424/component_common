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
public class CallableLocalCacheManager extends LocalCacheManager{	
	private Callable<Object> callable;
	private CallableLocalCacheManager(Cache<String, Object> cache,Callable<Object> callable){
		super(cache);
		this.callable = callable;
	}
	
	public static CallableLocalCacheManager getInstance(Callable<Object> callable){
		return getInstance(10000,24, TimeUnit.HOURS,callable);
	}
	public static CallableLocalCacheManager getInstance(int maxSize,long time,TimeUnit timeUnit,Callable<Object> callable){
		Cache<String, Object> newCache = CacheBuilder.newBuilder().maximumSize(maxSize)
				.expireAfterWrite(time,timeUnit).recordStats().build();
		return new CallableLocalCacheManager(newCache,callable);
	}
	
	/**
	 * 获取值，如果获取不了，使用defaultValue默认值，如果defaultValue为空，则抛异常
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws ExecutionException
	 */
	public Object get(String key){
		Object var = null;
		try{
			var = cache.get(key, callable);
		}catch(Exception e){
		}
		return var;
	}
}
