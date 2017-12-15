package com.maoshen.component.cache.test;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.maoshen.component.local.LocalCacheObject;
import com.maoshen.component.local.LocalCacheObjectUtils;

public class LocalCacheTest {
	public static void main(String []s){
		for (int i=0;i<20902;i++){
			LocalCacheObject.put(Integer.toString(i)+"", i);
		}
		System.out.println("ok,size:"+LocalCacheObject.size());
		
		Cache<String,Object> cache1 = LocalCacheObjectUtils.getInstance(500, 24, TimeUnit.HOURS);
		Cache<String,Object> cache2 = LocalCacheObjectUtils.getInstance(500, 24, TimeUnit.HOURS);
		for (int i=0;i<400;i++){
			LocalCacheObjectUtils.put(cache1,Integer.toString(i)+"", i);
		}
		for (int i=0;i<600;i++){
			LocalCacheObjectUtils.put(cache2,Integer.toString(i)+"", i);
		}
		System.out.println("ok,size:"+LocalCacheObjectUtils.size(cache1));
		System.out.println("ok,size:"+LocalCacheObjectUtils.size(cache2));
	}
}
