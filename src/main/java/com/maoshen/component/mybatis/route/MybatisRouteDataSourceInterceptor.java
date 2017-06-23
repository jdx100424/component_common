package com.maoshen.component.mybatis.route;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.maoshen.component.aop.interceptor.service.ServiceInterceptor;

@Intercepts({  
    @Signature(type = Executor.class, method = "update", args = {  
            MappedStatement.class, Object.class }),  
    @Signature(type = Executor.class, method = "query", args = {  
            MappedStatement.class, Object.class, RowBounds.class,  
            ResultHandler.class }) })  
public class MybatisRouteDataSourceInterceptor implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MybatisRouteInfo mybatisRouteInfo = MybatisRouteDataSourceHolder.get();
		if(mybatisRouteInfo != null && StringUtils.isNotBlank(mybatisRouteInfo.getRouteId())){
			//DAO有标记的话，分辨出哪个分表分库
			Map<Object, DataSource> dataSourceMap = MybatisRouteUtil.getResolvedDataSources();
			if(dataSourceMap!=null && dataSourceMap.isEmpty()==false){
				
			}
		}
		
		Object[] objects = invocation.getArgs();
		MappedStatement ms = (MappedStatement) objects[0];
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

}
