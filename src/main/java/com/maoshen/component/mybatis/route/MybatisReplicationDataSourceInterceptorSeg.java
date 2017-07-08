package com.maoshen.component.mybatis.route;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.mybatis.route.regular.MybatisRoute;
import com.maoshen.component.mybatis.route.regular.impl.DefaultMybatisRoute;


@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisReplicationDataSourceInterceptorSeg implements Interceptor,InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisReplicationDataSourceInterceptorSeg.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	private Integer routeTableCount;	
	
	private MybatisRoute mybatisRoute;
	
	public void setRouteTableCount(Integer routeTableCount) {
		this.routeTableCount = routeTableCount;
	}

	public void setMybatisRoute(MybatisRoute mybatisRoute) {
		this.mybatisRoute = mybatisRoute;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECTOR_FACTORY);
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");		
		
		if (StringUtils.isNotBlank(originalSql)) {
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
			String className = mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
			Class<?> classObj = Class.forName(className);

			//保存原本的参数符信息
			ArrayList<ParameterMapping> tempSqlParam = new ArrayList<ParameterMapping>();
			tempSqlParam.addAll((ArrayList<ParameterMapping>)boundSql.getParameterMappings());

			// 根据配置自动生成分表SQL
			TableSeg tableSeg = classObj.getAnnotation(TableSeg.class);
			LOGGER.info(JSONObject.toJSONString(boundSql.getParameterObject()));

			if(tableSeg!=null){
				//分表不分库
				Map<String,Object> paramterMap = (Map<String, Object>) boundSql.getParameterObject();
				String shardBy = tableSeg != null ? tableSeg.shardBy().trim() : "";
				String tableName = tableSeg != null ? tableSeg.tableName().trim() : "";
				if(StringUtils.isBlank(tableName) || StringUtils.isBlank(shardBy)){
					throw new Exception("tableSeg tableName or shardBy is not allow null");
				}
				//如果参数没有包含路由ID，则查所有的表UNION ALL，否则获取对应列的路由数字，查询
				Object shardByValue =null;
				if(paramterMap != null ){
					try{
						shardByValue = paramterMap.get(shardBy);
					}catch(Exception e){
					}
				}
				if(shardByValue ==null || StringUtils.isBlank(shardByValue.toString())){
					if(mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
						//构建多个？参数
						originalSql = mybatisRoute.getUnionSql(originalSql,tableName,routeTableCount);
						metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);	
						prepared(tempSqlParam, boundSql);
						metaStatementHandler.setValue("delegate.boundSql",boundSql);
						return invocation.proceed();
					}
					throw new Exception("非select语句，路由值不能为空");
				}else{
					if(shardByValue instanceof Integer){
						long route = mybatisRoute.getRouteNumberIntTable((int)shardByValue,routeTableCount);
						if(mybatisRoute.isFirstTable(route,routeTableCount)==false){
							originalSql = originalSql.replaceAll(tableName, tableName+route);
							metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
						}
						return invocation.proceed();
					}else if(shardByValue instanceof Long){
						long route = mybatisRoute.getRouteNumberLongTable((long)shardByValue,routeTableCount);
						if(mybatisRoute.isFirstTable(route,routeTableCount)==false){
							originalSql = originalSql.replaceAll(tableName, tableName+route);
							metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
						}
						return invocation.proceed();
					}else if(shardByValue instanceof String){
						long route = mybatisRoute.getRouteNumberStringTable(shardByValue.toString(),routeTableCount);
						if(mybatisRoute.isFirstTable(route,routeTableCount)==false){
							originalSql = originalSql.replaceAll(tableName, tableName+route);
							metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
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

	private void prepared(ArrayList<ParameterMapping> tempSqlParam,BoundSql boundSql) throws SQLException {
		try{
			ArrayList<ParameterMapping> addList = new ArrayList<ParameterMapping>();
			//非自身的时候，增加N－1个参数
			for(int i=0;i<routeTableCount;i++){
				if(mybatisRoute.isFirstTable(i,routeTableCount)==false){
					for(ParameterMapping pm:tempSqlParam){
						MetaObject javaBeanMeta = MetaObject.forObject(pm,
				                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
						
						Configuration configuration = (Configuration) javaBeanMeta.getValue("configuration");
						String key =  (String) javaBeanMeta.getValue("property") ;
						String property = key ;
						Class<?> javaType = (Class<?>) javaBeanMeta.getValue("javaType");
						
						ParameterMapping newPm = new ParameterMapping.Builder(configuration,property,javaType).build();
						//预编译参数设置
						addList.add(newPm);
					}
				}
			}
			tempSqlParam.addAll(addList);
			MetaObject boundSqlMeta = MetaObject.forObject(boundSql,DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			boundSqlMeta.setValue("parameterMappings",tempSqlParam);
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
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
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(routeTableCount==null || routeTableCount<=0){
			routeTableCount =1;
		}
		if(mybatisRoute==null){
			LOGGER.info("mybatisRoute is null,use defaultImplements DefaultMybatisRoute");	
			mybatisRoute = new DefaultMybatisRoute();
		}else{
			LOGGER.info("mybatisRoute is not null,use user custom MybatisRoute");	
		}
	}
}
