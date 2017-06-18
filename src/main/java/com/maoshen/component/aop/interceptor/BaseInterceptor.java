package com.maoshen.component.aop.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * 拦截器
 */
public abstract class BaseInterceptor {
	public abstract Object around(ProceedingJoinPoint pjp) throws Throwable;

	/**
	 * 使用标记定义切割范围 例如 @Pointcut("execution(* com.xx.xx.service..*.*(..))")
	 */
	public abstract void pointcut();

	/**
	 * 服务名称
	 * 
	 * @return
	 */
	public abstract String getServiceName();
}
