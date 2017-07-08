package com.maoshen.component.mybatis.route.regular.impl;

import com.maoshen.component.mybatis.route.regular.MybatisRoute;

/**
 * 分表路由默认实现
 * @author daxian.jianglifesense.com
 *
 */
public class DefaultMybatisRoute extends MybatisRoute{

	@Override
	public long getRouteNumberIntTable(Integer id, Integer routeTableCount) throws Exception {
		return getRouteNumberLongTable((long)id,routeTableCount);
	}

	@Override
	public long getRouteNumberLongTable(Long id, Integer routeTableCount) throws Exception {
		if(id==null || id<=0){
			throw new Exception("id不能为空");
		}
		if(routeTableCount==null || routeTableCount<=0){
			throw new Exception("routeTableCount不能为空");
		}
		//余数划分
		long result = id % routeTableCount;
		return result;
	}

	@Override
	public long getRouteNumberStringTable(String id, Integer routeTableCount) throws Exception {
		//ID是字符串的话，默认按照HASHCODE的值，用INT取余数划分
		int hashId = id.hashCode();
		return getRouteNumberIntTable(hashId,routeTableCount);
	}

}
