package com.maoshen.component.aop.interceptor.service;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoshen.component.aop.interceptor.BaseInterceptor;

public abstract class ServiceInterceptor extends BaseInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterceptor.class);


	/**
	 * 业务拦截器，在指定时间内，增加对相同的请求ID进行拦截，
	 */
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();

		try {
			Date startTime = null;
			Date endTime = null;
			if (isShowRunningTime()) {
				startTime = new Date();
			}

			// 原来的逻辑运行
			Object result = pjp.proceed();

			if (isShowRunningTime()) {
				endTime = new Date();
				LOGGER.info(getServiceName() + "_service method:{},startDate:{},endDate:{},times:{}", method.getName(),
						startTime, endTime, (endTime.getTime() - startTime.getTime()) / 1000);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(getServiceName() + "_service method:{} exception", method.getName(), e);
			throw e;
		}
	}

	/**
	 * 时候显示运行SERVICE时间
	 * 
	 * @return
	 */
	public boolean isShowRunningTime() {
		return true;
	}
}
