package com.maoshen.component.mybatis.route;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.mybatis.MybatisReplicationDataSourceHolder;
import com.maoshen.component.mybatis.MybatisRouteUtil;


@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisReplicationDataSourceInterceptorSeg implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisReplicationDataSourceInterceptorSeg.class);
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
		if (StringUtils.isNotBlank(originalSql)) {
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String id = mappedStatement.getId();
			String className = id.substring(0, id.lastIndexOf("."));
			Class<?> classObj = Class.forName(className);

			// 根据配置自动生成分表SQL
			TableSeg tableSeg = classObj.getAnnotation(TableSeg.class);
			DbSeg dbSeg = classObj.getAnnotation(DbSeg.class);
			LOGGER.info(JSONObject.toJSONString(boundSql.getParameterObject()));

			//分库不分表
			if (dbSeg != null) {
				Object ParamObj = boundSql.getParameterObject();
				String s = JSONObject.toJSONString(ParamObj);
				Map<String,Object> ParamterMap = JSONObject.parseObject(s, Map.class);
				LOGGER.info(JSONObject.toJSONString(ParamterMap));
				String shardBy = dbSeg != null ? dbSeg.shardBy().trim() : "";
				if(StringUtils.isBlank(shardBy)){
					throw new Exception("标签分库字段不能为NULL");
				}
				Object shardByValue = ParamterMap.get(shardBy);
				if(shardByValue == null || StringUtils.isBlank(shardByValue.toString())){
					throw new Exception("分库字段值不能为NULL");
				}
				if(shardByValue instanceof Integer){
					String dataSourceKeyName = MybatisRouteUtil.getDataSourceKeyName((int)shardByValue);
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(dataSourceKeyName);
					return invocation.proceed();
				}else if(shardByValue instanceof Long){
					String dataSourceKeyName = MybatisRouteUtil.getDataSourceKeyName((long)shardByValue);
					MybatisReplicationDataSourceHolder.getDataSource().setDataSourceName(dataSourceKeyName);
					return invocation.proceed();
				}else{
					throw new Exception("此字段类型不支持");
				}
			}else if(tableSeg!=null){
				//分表不分库
				Object ParamObj = boundSql.getParameterObject();
				String s = JSONObject.toJSONString(ParamObj);
				Map<String,Object> ParamterMap = JSONObject.parseObject(s, Map.class);
				LOGGER.info(JSONObject.toJSONString(ParamterMap));
				String shardBy = tableSeg != null ? tableSeg.shardBy().trim() : "";
				String tableName = tableSeg != null ? tableSeg.tableName().trim() : "";
				if(StringUtils.isBlank(tableName) || StringUtils.isBlank(shardBy)){
					throw new Exception("tableSeg tableName or shardBy is not allow null");
				}
				//如果参数没有包含路由ID，则查所有的表UNION ALL，否则获取对应列的路由数字，查询
				Object shardByValue = ParamterMap.get(shardBy);
				if(shardByValue ==null || StringUtils.isBlank(shardByValue.toString())){
					if(mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
						String newSql = MybatisRouteUtil.getUnionSql(originalSql,tableName);
						metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
						return invocation.proceed();
					}
					throw new Exception("非select语句，路由值不能为空");
				}else{
					if(shardByValue instanceof Integer){
						long route = MybatisRouteUtil.getRouteNumberTable((int)shardByValue);
						if(MybatisRouteUtil.isFirstTable(route)==false){
							String newSql = originalSql.replaceAll(tableName, tableName+route);
							metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
						}
						return invocation.proceed();
					}else if(shardByValue instanceof Long){
						long route = MybatisRouteUtil.getRouteNumberTable((long)shardByValue);
						if(MybatisRouteUtil.isFirstTable(route)==false){
							String newSql = originalSql.replaceAll(tableName, tableName+route);
							metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
						}
						return invocation.proceed();
					}else{
						throw new Exception("此字段类型不支持");
					}
				}
			}else{
				//没有标记的话，直接返回
				return invocation.proceed();
			}
		}else{
			throw new Exception("sql is not allow null");
		}
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
