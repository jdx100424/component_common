package com.maoshen.component.thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TestCallable {
	public static void main(String[] args) {
		TestCallable tc = new TestCallable();
		ThreadDemo td = tc.new ThreadDemo();

		// 1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
		FutureTask<Integer> result = new FutureTask<>(td);
		new Thread(result).start();

		// 2.接收线程运算后的结果
		try {
			Integer sum = result.get(); // FutureTask 可用于 闭锁
										// 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
			System.out.println(sum);
			System.out.println("------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ThreadDemo implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			int sum = 0;
			for (int i = 0; i <= 100; i++) {
				sum += i;
			}
			return sum;
		}
	}
}
