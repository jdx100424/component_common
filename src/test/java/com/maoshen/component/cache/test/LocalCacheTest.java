package com.maoshen.component.cache.test;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.maoshen.component.local.LocalCacheObjectExample;
import com.maoshen.component.local.LocalCacheObjectUtils;

public class LocalCacheTest {
	public static void main(String []s){
		LocalCacheObjectExample.put(null,2);
		for (int i=0;i<20902;i++){
			LocalCacheObjectExample.put(Integer.toString(i)+"", i);
		}
		System.out.println("ok,size:"+LocalCacheObjectExample.size());
		
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
