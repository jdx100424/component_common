package com.maoshen.component.aop.interceptor.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.maoshen.component.aop.interceptor.BaseInterceptor;
import com.maoshen.component.sentry.SentryProvider;

import io.sentry.event.Event;

public abstract class DaoInterceptor extends BaseInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoInterceptor.class);

	/**
	 * SPRING DAO MYBATIS拦截器，如果访问DB出错，除了唯一索引和主键唯一，其他的都往SENTRY发告警
	 */
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String method = pjp.getSignature().getName();
		try {
			// 原来的逻辑运行
			Object result = pjp.proceed();
			return result;
		}catch (Exception e) {
			if(e instanceof DuplicateKeyException == false){
				SentryProvider.sendLog(getServiceName(), "method:"+method+",visit db is error", Event.Level.ERROR, LOGGER);
			}
			LOGGER.error(getServiceName() + "_dao method:{} exception", method, e);
			throw e;
		}
	}
}
