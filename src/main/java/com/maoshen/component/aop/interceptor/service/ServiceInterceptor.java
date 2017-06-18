package com.maoshen.component.aop.interceptor.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.aop.interceptor.BaseInterceptor;
import com.maoshen.component.mybatis.Master;
import com.maoshen.component.mybatis.MybatisReplicationDataSourceHolder;
import com.maoshen.component.mybatis.MybatisReplicationInfo;
import com.maoshen.component.mybatis.Slave;
import com.maoshen.component.rest.UserRestContext;
import com.maoshen.component.rpc.filter.constant.DubboContextFilterConstant;

public abstract class ServiceInterceptor extends BaseInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterceptor.class);

	/**
	 * 业务拦截器，在指定时间内，增加对相同的请求ID进行拦截，
	 */
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object targetObject = pjp.getTarget();
		String method = pjp.getSignature().getName();

		Class<?>[] clazz = targetObject.getClass().getInterfaces();
		Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put(DubboContextFilterConstant.RPC_USER_REST_CONTEXT, JSONObject.toJSONString(UserRestContext.get()));
			RpcContext.getContext().setAttachments(map);

			MybatisReplicationInfo mybatisReplicationInfo = new MybatisReplicationInfo();
			for(int i=0;i<clazz.length;i++){
				Method m = clazz[i].getMethod(method, parameterTypes);
				if (null != m) {
						if (m.isAnnotationPresent(Master.class)) {
						mybatisReplicationInfo.setMaster(true);
						break;
					} else if (m.isAnnotationPresent(Slave.class)) {
						mybatisReplicationInfo.setSlave(true);
						break;
					} else if (m.isAnnotationPresent(Transactional.class)) {
						mybatisReplicationInfo.setMaster(true);
						break;
					}	
				}
			}
			MybatisReplicationDataSourceHolder.putDataSource(mybatisReplicationInfo);
			LOGGER.info("==============" + JSONObject.toJSONString(mybatisReplicationInfo) + "=============");

			// 原来的逻辑运行
			Object result = pjp.proceed();
			return result;
		} catch (Exception e) {
			LOGGER.error(getServiceName() + "_service method:{} exception", method, e);
			throw e;
		}
	}
}
