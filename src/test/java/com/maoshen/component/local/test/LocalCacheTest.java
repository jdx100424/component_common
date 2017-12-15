package com.maoshen.component.local.test;

import java.util.concurrent.ExecutionException;

import com.maoshen.component.local.LocalCacheObject;

public class LocalCacheTest {
	public static void main(String []s) throws ExecutionException{
		LocalCacheObject.put("1", "2");
		System.out.println(LocalCacheObject.get("1","jdx1"));
		System.out.println(LocalCacheObject.get("3","jdx2"));
		System.out.println(LocalCacheObject.get("5"));
	}
}
