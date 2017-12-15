package com.maoshen.component.local.test;

import java.util.concurrent.ExecutionException;

import com.maoshen.component.local.LocalCacheObjectExample;

public class LocalCacheTest {
	public static void main(String []s) throws ExecutionException{
		LocalCacheObjectExample.put("1", "2");
		System.out.println(LocalCacheObjectExample.get("1","jdx1"));
		System.out.println(LocalCacheObjectExample.get("3","jdx2"));
		System.out.println(LocalCacheObjectExample.get("5"));
	}
}
