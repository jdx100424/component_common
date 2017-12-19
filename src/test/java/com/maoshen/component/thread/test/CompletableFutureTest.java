package com.maoshen.component.thread.test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CompletableFutureTest {
	private static Random rand = new Random();

	private static long t = System.currentTimeMillis();

	static int getMoreData() {
		System.out.println("begin to start compute");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t) / 1000 + " seconds");
		return rand.nextInt(1000);

	}

	public static void main(String[] args) throws Exception {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(CompletableFutureTest::getMoreData);
		Future<Integer> f = future.whenComplete((v, e) -> {
			System.out.println("v:" + v);
			System.out.println("e:" + e);
		});

		System.out.println(f.get());
		// System.in.read();

		System.out.println("**********************");
		CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
			return 100;
		});
		CompletableFuture<String> f2 = future2.thenCompose(i -> {
			return CompletableFuture.supplyAsync(() -> {
				return (i * 10) + "";
			});

		});
		System.out.println(f2.get()); // 1000
		
		System.out.println("**********************");
		CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
		    return 100;
		});
		CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
		    return "abc";
		});
		CompletableFuture<String> f3 =  future4.thenCombine(future3, (x,y) -> y + "-" + x);
		System.out.println(f3.get()); //abc-100
	}
}
