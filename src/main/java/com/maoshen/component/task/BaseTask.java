package com.maoshen.component.task;

import java.util.concurrent.TimeUnit;

public abstract class BaseTask{
	/**
	 * 分布式默认超时的单位，秒
	 */
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	/**
	 * 分布式超时假如如用户不写，默认是10秒
	 */
	public static final Long DEFAULT_EXPIRE_TIME = 10L;
	
	/**
	 * 分布式锁默认超时时间
	 */
	public abstract Long getExpireTime();
	/**
	 * 定时器在REDIS的KEY名称
	 * @return
	 */
	public abstract String getName();
	/**
	 * 定时器实现类
	 */
	public abstract void timeTaskRun();
}
