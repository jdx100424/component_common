package com.maoshen.component.mybatis;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

public class MybatisRouteUtil {
	private static Map<Object, DataSource> resolvedDataSources;
	private static DataSource resolvedDefaultDataSource;
	private static final long IS_FIRST = 0;
	private static Long routeTableCount;
	
	public static void setRouteTableCount(Long value){
		routeTableCount = value;
	}
	
	public static DataSource getResolvedDefaultDataSource() {
		return resolvedDefaultDataSource;
	}

	public static void setResolvedDefaultDataSource(DataSource resolvedDefaultDataSource) {
		MybatisRouteUtil.resolvedDefaultDataSource = resolvedDefaultDataSource;
	}

	public static Map<Object, DataSource> getResolvedDataSources() {
		return resolvedDataSources;
	}

	public static void setResolvedDataSources(Map<Object, DataSource> resolvedDataSources) {
		MybatisRouteUtil.resolvedDataSources = resolvedDataSources;
	}
	
	public static String getDataSourceKeyName(Integer id) throws Exception{
		return getDataSourceKeyName((long)id);
	}
	public static String getDataSourceKeyName(Long id) throws Exception{
		long routeInt = getRouteNumberDb(id);
		Iterator<Entry<Object, DataSource>> it = resolvedDataSources.entrySet().iterator();
		long k = 0;
		while(it.hasNext()){
			Entry<Object, DataSource> e = it.next();
			if(k==routeInt){
				return e.getKey().toString();
			}
			k++;
		}
		throw new Exception("getDataSourceKeyName fail");
	}

	public static long getRouteNumberTable(Long id) throws Exception{
		if(id==null || id<=0){
			throw new Exception("id不能为空");
		}
		if(routeTableCount==null || routeTableCount<=0){
			throw new Exception("routeTableCount不能为空");
		}
		long result = id % routeTableCount;
		return result;
	}
	public static long getRouteNumberTable(Integer id) throws Exception{
		return getRouteNumberTable((long)id);
	}
	/**
	 * 根据主键ID，获取fenku路由值
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static long getRouteNumberDb(Long id) throws Exception{
		if(id==null || id<=0){
			throw new Exception("id不能为空");
		}
		if(resolvedDataSources==null || resolvedDataSources.isEmpty()){
			throw new Exception("resolvedDataSources不能为空");
		}
		long result = id % resolvedDataSources.size();
		return result;
	}
	public static long getRouteNumberDb(Integer id) throws Exception{
		return getRouteNumberDb((long)id);
	}
	
	
	/**
	 * 判断是否为第一个表（原来的名字）
	 * @param route
	 * @return
	 */
	public static boolean isFirstTable(long route){
		long result = route % routeTableCount;
		if(result == IS_FIRST){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取unionSql
	 * @param sql
	 * @param tableName
	 * @return
	 */
	public static String getUnionSql(String sql,String tableName){
		StringBuilder newSql = new StringBuilder();
		newSql.append(sql);
		for(int i=0;i<routeTableCount;i++){
			//第一条默认是第一个表
			if(i!=IS_FIRST){
				newSql.append(" union all ");
				String unionSql = sql.replaceAll(tableName, tableName+i);
				newSql.append(unionSql);
			}
		}
		return newSql.toString();
	}
}
