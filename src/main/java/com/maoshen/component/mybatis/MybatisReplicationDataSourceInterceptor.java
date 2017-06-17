package com.maoshen.component.mybatis;

import java.util.Properties;

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

import com.maoshen.component.aop.interceptor.service.ServiceInterceptor;

@Intercepts({  
    @Signature(type = Executor.class, method = "update", args = {  
            MappedStatement.class, Object.class }),  
    @Signature(type = Executor.class, method = "query", args = {  
            MappedStatement.class, Object.class, RowBounds.class,  
            ResultHandler.class }) })  
public class MybatisReplicationDataSourceInterceptor implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] objects = invocation.getArgs();
		MappedStatement ms = (MappedStatement) objects[0];
		// 有强制指定了，直接设置
		if (MybatisReplicationDataSourceHolder.getMasterOrSlave().isMaster()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("is use master by forse");
			}
			MybatisReplicationDataSourceHolder.getMasterOrSlave()
					.setDataSourceName(MybatisReplicationDataSourceHolder.MASTER);
			return invocation.proceed();
		} else if (MybatisReplicationDataSourceHolder.getMasterOrSlave().isSlave()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("is use slave by forse");
			}
			MybatisReplicationDataSourceHolder.getMasterOrSlave()
					.setDataSourceName(MybatisReplicationDataSourceHolder.SLAVE);
			return invocation.proceed();
		}
		// 没有强制指定库的，根据SQL语句处理
		if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
			MybatisReplicationDataSourceHolder.getMasterOrSlave()
					.setDataSourceName(MybatisReplicationDataSourceHolder.SLAVE);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("is use slave by auto");
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("is use master by auto");
			}
			MybatisReplicationDataSourceHolder.getMasterOrSlave()
					.setDataSourceName(MybatisReplicationDataSourceHolder.MASTER);
		}
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
