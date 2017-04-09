package com.maoshen.component.other;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	/**
	 * 获取当天还剩下多少时间，单位秒，一般用于REDIS一天限制次数等
	 * @return
	 */
	public static long getTodayEndLastTime() {
		long result = getTodayEnd() - new Date().getTime();
		return result / 1000;
	}

	/**
	 * 获取当天时间结束时间
	 * 
	 * @return
	 */
	public static long getTodayEnd() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTimeInMillis();
	}

	/**
	 * 获取当前时间开始时间
	 * 
	 * @return
	 */
	public static long getTodayStart() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
}
