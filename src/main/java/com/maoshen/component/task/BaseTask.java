package com.maoshen.component.task;

public abstract class BaseTask{
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
