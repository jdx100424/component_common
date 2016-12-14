package com.maoshen.component.aop.interceptor.service;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.maoshen.component.aop.interceptor.BaseInterceptor;
import com.maoshen.component.aop.interceptor.service.exception.SameRequestIdException;
import com.maoshen.component.base.dto.RequestHeaderDto;

public abstract class ServiceInterceptor extends BaseInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterceptor.class);
	private static final long DEFAULT_LOCK_TIME = 60L;
	/**
	 * 分布式默认超时的单位，秒
	 */
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate jedisTemplate;

	/**
	 * 业务拦截器，在指定时间内，增加对相同的请求ID进行拦截，
	 */
	//@Around("pointcut()")
	@SuppressWarnings("unchecked")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		RequestHeaderDto header = null;
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Object[] args = pjp.getArgs();

		System.out.println(pjp.getTarget().getClass().getName());
		try {
			header = (RequestHeaderDto) args[0];
		} catch (Exception e) {
			LOGGER.warn(getServiceName() + "_service method:{} has not RequestHeader",method.getName());
		}

		try {
			if (header != null) {
				if (jedisTemplate.opsForValue().setIfAbsent(getServiceName()+ header.getRequestId(), "true") == false) {
					throw new SameRequestIdException(getServiceName() + "_service method:" + method.getName()
							+ " requestId:" + header.getRequestId() + " is same");
				} else {
					long time = (setLockTime() == 0 ? DEFAULT_LOCK_TIME : setLockTime());
					jedisTemplate.expire(getServiceName()+header.getRequestId(), time, TIME_UNIT);
				}
			}

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
	 * 有效时间，如为0，则默认60秒
	 * 
	 * @return
	 */
	public abstract long setLockTime();

	/**
	 * 时候显示运行SERVICE时间
	 * 
	 * @return
	 */
	public boolean isShowRunningTime() {
		return true;
	}
}
