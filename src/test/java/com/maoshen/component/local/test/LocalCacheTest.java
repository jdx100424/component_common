package com.maoshen.component.local.test;

import java.util.concurrent.ExecutionException;

import com.maoshen.component.local.LocalCache;

public class LocalCacheTest {
	public static void main(String []s) throws ExecutionException{
		LocalCache.put("1", "2");
		System.out.println(LocalCache.get("1","jdx1"));
		System.out.println(LocalCache.get("3","jdx2"));
		System.out.println(LocalCache.get("5"));
	}
}
