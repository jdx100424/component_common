package com.maoshen.component.mybatis.route;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.mybatis.MybatisReplicationDataSourceHolder;
import com.maoshen.component.mybatis.MybatisRouteUtil;


@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisReplicationDataSourceInterceptorSeg implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisReplicationDataSourceInterceptorSeg.class);
	private static final String tag = MybatisReplicationDataSourceInterceptorSeg.class.getName();
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY);
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
		//boundSql,parameterObject Map {id=1, param1=1}

		Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
		if (originalSql != null && !originalSql.equals("")) {
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String id = mappedStatement.getId();
			String className = id.substring(0, id.lastIndexOf("."));
			Class<?> classObj = Class.forName(className);

			// 根据配置自动生成分表SQL
			TableSeg tableSeg = classObj.getAnnotation(TableSeg.class);
			DbSeg dbSeg = classObj.getAnnotation(DbSeg.class);
			LOGGER.info(JSONObject.toJSONString(boundSql.getParameterObject()));

			//分库，表相同
			if (dbSeg != null) {
				Object ParamObj = boundSql.getParameterObject();
				String s = JSONObject.toJSONString(ParamObj);
				Map<String,Object> ParamterMap = JSONObject.parseObject(s, Map.class);
				LOGGER.info(JSONObject.toJSONString(ParamterMap));
				//String newSql = "";
				String shardBy = dbSeg != null ? dbSeg.shardBy().trim() : "";
				//没有配置分库字段，或者分库字段在SQL参数找不到的话，用默认的
				if(StringUtils.isBlank(shardBy)){
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(null);
					return invocation.proceed();
				}
				Object shardByValue = ParamterMap.get(shardBy);
				if(shardByValue == null || StringUtils.isBlank(shardByValue.toString())){
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(null);
					return invocation.proceed();
				}
				if(shardByValue instanceof Integer){
					//long resultRoute = MybatisRouteUtil.getRouteNumber((int)shardByValue);
					String dataSourceKeyName = MybatisRouteUtil.getDataSourceKeyName((int)shardByValue);
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(dataSourceKeyName);
					return invocation.proceed();
				}else if(shardByValue instanceof Long){
					String dataSourceKeyName = MybatisRouteUtil.getDataSourceKeyName((long)shardByValue);
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(dataSourceKeyName);
					return invocation.proceed();
				}
				//metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
			}
		}
		//如无法匹配，强制使用使用默认的
		MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(null);

		// 传递给下一个拦截器处理
		return invocation.proceed();

	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的
		// 次数
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties arg0) {

	}
}
