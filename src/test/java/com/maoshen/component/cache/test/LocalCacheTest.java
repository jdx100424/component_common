package com.maoshen.component.cache.test;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.maoshen.component.local.LocalCacheObjectExample;
import com.maoshen.component.local.LocalCacheManager;

public class LocalCacheTest {
	public static void main(String []s){
		for (int i=0;i<20902;i++){
			LocalCacheObjectExample.put(Integer.toString(i)+"", i);
		}
		System.out.println("ok,size:"+LocalCacheObjectExample.size());
		
		LocalCacheManager cache1 = LocalCacheManager.getInstance(500, 24, TimeUnit.HOURS);
		LocalCacheManager cache2 = LocalCacheManager.getInstance(500, 24, TimeUnit.HOURS);
		for (int i=0;i<400;i++){
			cache1.put(Integer.toString(i)+"", i);
		}
		for (int i=0;i<600;i++){
			cache2.put(Integer.toString(i)+"", i);
		}
		System.out.println("ok,size:"+cache1.size());
		System.out.println("ok,size:"+cache2.size());
	}
}
