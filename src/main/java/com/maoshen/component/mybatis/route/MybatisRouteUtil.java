package com.maoshen.component.mybatis.route;

import java.util.Map;

import javax.sql.DataSource;

public class MybatisRouteUtil {
	private static Map<Object, DataSource> resolvedDataSources;

	public static Map<Object, DataSource> getResolvedDataSources() {
		return resolvedDataSources;
	}

	public static void setResolvedDataSources(Map<Object, DataSource> resolvedDataSources) {
		MybatisRouteUtil.resolvedDataSources = resolvedDataSources;
	}
	
	/**
	 * 根据主键ID，获取路由值
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static int getRouteNumber(Integer id) throws Exception{
		if(id==null || id<=0){
			throw new Exception("id不能为空");
		}
		if(resolvedDataSources==null || resolvedDataSources.isEmpty()){
			throw new Exception("resolvedDataSources不能为空");
		}
		int result = id % resolvedDataSources.size();
		return result;
	}
}
