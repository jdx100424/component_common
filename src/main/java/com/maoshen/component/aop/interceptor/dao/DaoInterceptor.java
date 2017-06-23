package com.maoshen.component.aop.interceptor.dao;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoshen.component.aop.interceptor.BaseInterceptor;
import com.maoshen.component.mybatis.route.MybatisRoute;
import com.maoshen.component.mybatis.route.MybatisRouteDataSourceHolder;
import com.maoshen.component.mybatis.route.MybatisRouteInfo;

public abstract class DaoInterceptor extends BaseInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoInterceptor.class);

	/**
	 * DAO拦截器，如果对于标记分表分库的DAO @MybatisRoute,方法，需要拦截
	 */
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object targetObject = pjp.getTarget();
		String method = pjp.getSignature().getName();
		try{
			Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
			Method m = targetObject.getClass().getMethod(method, parameterTypes);
			MybatisRouteInfo mybatisRouteInfo = new MybatisRouteInfo();
			if (m.isAnnotationPresent(MybatisRoute.class)) {
				MybatisRoute mybatisRoute = m.getAnnotation(MybatisRoute.class);
				String id = mybatisRoute.id();
				mybatisRouteInfo.setRouteId(id);
			}
			MybatisRouteDataSourceHolder.put(mybatisRouteInfo);
			// 原来的逻辑运行
			Object result = pjp.proceed();
			return result;
		} catch (Exception e) {
			LOGGER.error(getServiceName() + "_service method:{} exception", method, e);
			throw e;
		}
	}
}
