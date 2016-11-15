package com.maoshen.component.other;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步执行工具类
 * 
 */
public class LsExecutorService {
	private static int threads = Runtime.getRuntime().availableProcessors();// 默认线程数
	private static ExecutorService executorService;// 线程池
	private static ScheduledExecutorService scheduledExecutorService;// 计划任务线程池

	private LsExecutorService() {
	}

	/**
	 * 异步执行一次任务
	 * 
	 * @param runnable
	 *            任务
	 */
	public static void execute(Runnable runnable) {
		getExecutorService().execute(runnable);
	}

	/**
	 * 异步周期性执行任务
	 * 
	 * @param runnable
	 *            任务
	 * @param delay
	 *            延迟时间，单位：毫秒
	 * @param period
	 *            周期，单位：毫秒
	 */
	public static void executePeriod(Runnable runnable, long delay, long period) {
		getScheduledExecutorService().scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 异步延迟执行一次任务
	 * 
	 * @param runnable
	 *            任务
	 * @param delay
	 *            延迟时间，单位：毫秒
	 */
	public static void executeDelay(Runnable runnable, long delay) {
		getScheduledExecutorService().schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 获取线程池
	 * 
	 * @return
	 */
	private static ExecutorService getExecutorService() {
		if (executorService == null || executorService.isShutdown()) {
			executorService = Executors.newFixedThreadPool(threads);
		}
		return executorService;
	}

	/**
	 * 获取计划任务线程池
	 * 
	 * @return
	 */
	private static ScheduledExecutorService getScheduledExecutorService() {
		if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
			scheduledExecutorService = Executors.newScheduledThreadPool(threads);
		}
		return scheduledExecutorService;
	}
}
